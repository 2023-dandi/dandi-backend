package dandi.dandi.pushnotification.application.service;

import dandi.dandi.errormessage.application.port.out.ErrorMessageSender;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Location;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.application.port.out.webpush.WebPushManager;
import dandi.dandi.pushnotification.application.service.message.WeatherPushNotificationMessageGenerator;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationSource;
import dandi.dandi.pushnotification.domain.RetryableWeatherPushNotification;
import dandi.dandi.weather.application.port.out.WeatherForecastInfoManager;
import dandi.dandi.weather.application.port.out.WeatherForecastResult;
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
    private static final String FAILED_WEATHER_RESULT_MESSAGE_FORMAT = "날씨 정보 요청 실패 (memberId : %d) / %s";

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
                                                      List<RetryableWeatherPushNotification> retryableFailurePushNotifications) {
        WeatherForecastResult result = weatherForecastInfoManager.getForecasts(LocalDate.now(), location);
        if (result.isRetryableFailure()) {
            retryableFailurePushNotifications.add(RetryableWeatherPushNotification.of(pushNotification, location));
            return;
        } else if (result.isNonRetryableFailure()) {
            handleFailedWeatherResult(pushNotification.getMemberId(), result.getErrorMessage());
        }
        String pushMessage = weatherPushNotificationMessageGenerator.generateMessage(result);
        messages.add(new PushNotificationSource(pushNotification.getToken(), pushMessage));
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
        WeatherForecastResult result = weatherForecastInfoManager.getForecasts(LocalDate.now(),
                retryable.getLocation());
        if (result.isFailed()) {
            handleFailedWeatherResult(retryable.getMemberId(), result.getErrorMessage());
        }
        String pushMessage = weatherPushNotificationMessageGenerator.generateMessage(result);
        messages.add(new PushNotificationSource(retryable.getToken(), pushMessage));
    }

    private void handleFailedWeatherResult(Long memberId, String errorMessage) {
        String errorMessageToAdmin = String.format(FAILED_WEATHER_RESULT_MESSAGE_FORMAT, memberId, errorMessage);
        logger.error(errorMessageToAdmin);
        errorMessageSender.sendMessage(errorMessageToAdmin);
    }
}
