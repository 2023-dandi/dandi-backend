package dandi.dandi.pushnotification.application.service;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Location;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.application.port.out.webpush.WebPushManager;
import dandi.dandi.pushnotification.application.service.message.WeatherPushNotificationMessageGenerator;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.weather.application.port.out.WeatherForecastInfoManager;
import dandi.dandi.weather.application.port.out.WeatherForecastResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PushNotificationSender {

    private final PushNotificationPersistencePort pushNotificationPersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final WebPushManager webPushManager;
    private final WeatherForecastInfoManager weatherForecastInfoManager;
    private final WeatherPushNotificationMessageGenerator weatherPushNotificationMessageGenerator;
    private final String weatherPushTitle;

    public PushNotificationSender(PushNotificationPersistencePort pushNotificationPersistencePort,
                                  MemberPersistencePort memberPersistencePort, WebPushManager webPushManager,
                                  WeatherForecastInfoManager weatherForecastInfoManager,
                                  WeatherPushNotificationMessageGenerator weatherPushNotificationMessageGenerator,
                                  @Value("${weather.push.title}") String weatherPushTitle) {
        this.pushNotificationPersistencePort = pushNotificationPersistencePort;
        this.memberPersistencePort = memberPersistencePort;
        this.weatherForecastInfoManager = weatherForecastInfoManager;
        this.weatherPushNotificationMessageGenerator = weatherPushNotificationMessageGenerator;
        this.webPushManager = webPushManager;
        this.weatherPushTitle = weatherPushTitle;
    }

    @Transactional(readOnly = true)
    public void pushWeatherNotification(Long memberId) {
        PushNotification pushNotification = pushNotificationPersistencePort.findPushNotificationByMemberId(memberId)
                .orElseThrow(() -> InternalServerException.pushNotificationNotFound(memberId));
        if (pushNotification.isAllowed()) {
            Location location = memberPersistencePort.findLocationById(memberId)
                    .orElseThrow(() -> InternalServerException.withdrawnMemberPushNotification(memberId));
            WeatherForecastResponse weatherForecastResponse = weatherForecastInfoManager.getForecasts(LocalDate.now(),
                    location);
            String pushMessageBody = weatherPushNotificationMessageGenerator.generateMessage(weatherForecastResponse);
            PushNotificationSource pushNotificationSource =
                    new PushNotificationSource(pushNotification.getToken(), pushMessageBody);
            webPushManager.pushMessages(weatherPushTitle, List.of(pushNotificationSource));
        }
    }
}
