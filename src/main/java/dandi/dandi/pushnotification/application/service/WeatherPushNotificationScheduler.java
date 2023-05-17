package dandi.dandi.pushnotification.application.service;

import dandi.dandi.errormessage.application.port.out.ErrorMessageSender;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Location;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.application.port.out.webpush.WebPushManager;
import dandi.dandi.pushnotification.application.service.message.WeatherPushNotificationMessageGenerator;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.RetryableWeatherPushNotification;
import dandi.dandi.weather.application.port.out.WeatherForecastInfoManager;
import dandi.dandi.weather.application.port.out.WeatherForecastResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class WeatherPushNotificationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(WeatherPushNotificationScheduler.class);
    private static final int PUSH_NUMBER_UNIT = 10;
    private static final String EVERY_SEVEN_AM = "0 0 7 * * *";
    private static final String PUSH_NOTIFICATION_SEND_FAILURE_MESSAGE_FORMAT =
            "회원(memberId : %d) 날씨 푸시 알림 전송 실패 / %s";

    private final PushNotificationPersistencePort pushNotificationPersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final WeatherForecastInfoManager weatherForecastInfoManager;
    private final WeatherPushNotificationMessageGenerator weatherPushNotificationMessageGenerator;
    private final WebPushManager webPushManager;
    private final String weatherPushTitle;
    private final ErrorMessageSender errorMessageSender;

    public WeatherPushNotificationScheduler(PushNotificationPersistencePort pushNotificationPersistencePort,
                                            MemberPersistencePort memberPersistencePort,
                                            WeatherForecastInfoManager weatherForecastInfoManager,
                                            WeatherPushNotificationMessageGenerator weatherPushNotificationMessageGenerator,
                                            WebPushManager webPushManager,
                                            @Value("${weather.push.title}") String weatherPushTitle,
                                            ErrorMessageSender errorMessageSender) {
        this.pushNotificationPersistencePort = pushNotificationPersistencePort;
        this.memberPersistencePort = memberPersistencePort;
        this.weatherForecastInfoManager = weatherForecastInfoManager;
        this.weatherPushNotificationMessageGenerator = weatherPushNotificationMessageGenerator;
        this.webPushManager = webPushManager;
        this.weatherPushTitle = weatherPushTitle;
        this.errorMessageSender = errorMessageSender;
    }

    @Scheduled(cron = EVERY_SEVEN_AM)
    public void sendPushWeatherNotification() {
        Pageable pageable = PageRequest.of(0, PUSH_NUMBER_UNIT);
        List<RetryableWeatherPushNotification> retryableFailureWeatherPushNotification = new ArrayList<>();
        Slice<PushNotification> pushNotifications =
                pushNotificationPersistencePort.findAllowedPushNotification(pageable);
        pushWeatherNotification(pushNotifications.getContent(), retryableFailureWeatherPushNotification);
        while (pushNotifications.hasNext()) {
            pageable = pageable.withPage(pageable.getPageNumber() + 1);
            pushNotifications = pushNotificationPersistencePort.findAllowedPushNotification(pageable);
            pushWeatherNotification(pushNotifications.getContent(), retryableFailureWeatherPushNotification);
        }
        retry(retryableFailureWeatherPushNotification);
    }

    private void pushWeatherNotification(List<PushNotification> pushNotifications,
                                         List<RetryableWeatherPushNotification> retryableFailureWeatherPushNotification) {
        List<PushNotificationSource> messages = new ArrayList<>();
        for (PushNotification pushNotification : pushNotifications) {
            Optional<Location> location = memberPersistencePort.findLocationById(pushNotification.getMemberId());
            if (location.isEmpty()) {
                logger.info("탈퇴한 회원(memberId : {})의 푸시 알림 조회", pushNotification.getMemberId());
                continue;
            }
            addWeatherPushMessageOrHandleFailure(
                    pushNotification, location.get(), messages, retryableFailureWeatherPushNotification);
        }
        webPushManager.pushMessages(weatherPushTitle, messages);
    }

    private void addWeatherPushMessageOrHandleFailure(PushNotification pushNotification, Location location,
                                                      List<PushNotificationSource> messages,
                                                      List<RetryableWeatherPushNotification> retryableFailureWeatherPushNotification) {
        WeatherForecastResponse response = weatherForecastInfoManager.getForecasts(LocalDate.now(), location);
        if (response.isSuccess()) {
            String pushMessage = weatherPushNotificationMessageGenerator.generateMessage(response);
            messages.add(new PushNotificationSource(pushNotification.getToken(), pushMessage));
        } else if (response.isRetryableFailure()) {
            retryableFailureWeatherPushNotification.add(
                    RetryableWeatherPushNotification.of(pushNotification, location));
        } else if (response.isNonRetryableFailure()) {
            sendErrorMessageToAdmin(pushNotification.getMemberId(), response.getErrorMessage());
        }
    }

    private void retry(List<RetryableWeatherPushNotification> retryableFailureWeatherPushNotification) {
        List<PushNotificationSource> messages = new ArrayList<>();
        for (RetryableWeatherPushNotification retryable : retryableFailureWeatherPushNotification) {
            addNotificationSourceOrSendErrorMessageToAdmin(messages, retryable);
        }
        if (!messages.isEmpty()) {
            webPushManager.pushMessages(weatherPushTitle, messages);
        }
    }

    private void addNotificationSourceOrSendErrorMessageToAdmin(List<PushNotificationSource> messages,
                                                                RetryableWeatherPushNotification retryable) {
        WeatherForecastResponse response =
                weatherForecastInfoManager.getForecasts(LocalDate.now(), retryable.getLocation());
        if (response.isFailed()) {
            sendErrorMessageToAdmin(retryable.getMemberId(), response.getErrorMessage());
        }
        if (response.isSuccess()) {
            String pushMessage = weatherPushNotificationMessageGenerator.generateMessage(response);
            messages.add(new PushNotificationSource(retryable.getToken(), pushMessage));
        }
    }

    private void sendErrorMessageToAdmin(Long memberId, String errorMessage) {
        String errorMessageToAdmin = String.format(
                PUSH_NOTIFICATION_SEND_FAILURE_MESSAGE_FORMAT, memberId, errorMessage);
        errorMessageSender.sendMessage(errorMessageToAdmin);
    }
}
