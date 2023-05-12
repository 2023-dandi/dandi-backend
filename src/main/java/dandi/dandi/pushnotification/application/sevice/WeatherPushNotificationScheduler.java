package dandi.dandi.pushnotification.application.sevice;

import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Location;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.application.port.out.webpush.WebPushManager;
import dandi.dandi.pushnotification.application.sevice.message.WeatherPushNotificationMessageGenerator;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.weather.application.port.out.WeatherForecast;
import dandi.dandi.weather.application.port.out.WeatherForecastInfoManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final PushNotificationPersistencePort pushNotificationPersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final WeatherForecastInfoManager weatherForecastInfoManager;
    private final WeatherPushNotificationMessageGenerator weatherPushNotificationMessageGenerator;
    private final WebPushManager webPushManager;

    public WeatherPushNotificationScheduler(PushNotificationPersistencePort pushNotificationPersistencePort,
                                            MemberPersistencePort memberPersistencePort,
                                            WeatherForecastInfoManager weatherForecastInfoManager,
                                            WeatherPushNotificationMessageGenerator weatherPushNotificationMessageGenerator,
                                            WebPushManager webPushManager) {
        this.pushNotificationPersistencePort = pushNotificationPersistencePort;
        this.memberPersistencePort = memberPersistencePort;
        this.weatherForecastInfoManager = weatherForecastInfoManager;
        this.weatherPushNotificationMessageGenerator = weatherPushNotificationMessageGenerator;
        this.webPushManager = webPushManager;
    }

    @Scheduled(cron = EVERY_SEVEN_AM)
    public void sendPushWeatherNotification() {
        Pageable pageable = PageRequest.of(0, PUSH_NUMBER_UNIT);
        Slice<PushNotification> pushNotifications =
                pushNotificationPersistencePort.findAllowedPushNotification(pageable);
        pushWeatherNotification(pushNotifications.getContent());
        while (pushNotifications.hasNext()) {
            pageable = pageable.withPage(pageable.getPageNumber() + 1);
            pushNotifications = pushNotificationPersistencePort.findAllowedPushNotification(pageable);
            pushWeatherNotification(pushNotifications.getContent());
        }
    }

    private void pushWeatherNotification(List<PushNotification> pushNotifications) {
        for (PushNotification pushNotification : pushNotifications) {
            Optional<Location> location = memberPersistencePort.findLocationById(pushNotification.getMemberId());
            pushWeatherNotification(pushNotification, location);
        }
    }

    private void pushWeatherNotification(PushNotification pushNotification, Optional<Location> location) {
        if (location.isEmpty()) {
            logger.info("탈퇴한 회원(memberId : {})의 푸시 알림 조회", pushNotification.getMemberId());
            return;
        }
        WeatherForecast weatherForecast = weatherForecastInfoManager.getForecasts(LocalDate.now(), location.get());
        String token = pushNotification.getToken();
        String pushMessage = weatherPushNotificationMessageGenerator.generateMessage(weatherForecast);
        webPushManager.pushMessage(token, pushMessage);
    }
}
