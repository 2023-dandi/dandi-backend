package dandi.dandi.pushnotification.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Location;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.application.port.out.webpush.WebPushManager;
import dandi.dandi.pushnotification.application.service.message.WeatherPushNotificationMessageGenerator;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationTime;
import dandi.dandi.weather.application.port.out.WeatherForecast;
import dandi.dandi.weather.application.port.out.WeatherForecastInfoManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class WeatherPushNotificationSchedulerTest {

    private final PushNotificationPersistencePort pushNotificationPersistencePort =
            Mockito.mock(PushNotificationPersistencePort.class);
    private final MemberPersistencePort memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
    private final WeatherForecastInfoManager weatherForecastInfoManager =
            Mockito.mock(WeatherForecastInfoManager.class);
    private final WeatherPushNotificationMessageGenerator weatherPushNotificationMessageGenerator =
            Mockito.mock(WeatherPushNotificationMessageGenerator.class);
    private final WebPushManager webPushManager = Mockito.mock(WebPushManager.class);
    private final String weatherPushTitle = "푸시 알림 제목";

    private final WeatherPushNotificationScheduler weatherPushNotificationScheduler = new WeatherPushNotificationScheduler(
            pushNotificationPersistencePort, memberPersistencePort, weatherForecastInfoManager,
            weatherPushNotificationMessageGenerator, webPushManager, weatherPushTitle
    );

    @DisplayName("탈퇴한 회원에 대해 푸시 알림을 전송할수 없다.")
    @Test
    void sendPushWeatherNotification_withdrawnMember() {
        Pageable pageable = PageRequest.of(0, 10);
        PushNotification pushNotification =
                new PushNotification(1L, 1L, "token1", PushNotificationTime.initial(), true);
        when(pushNotificationPersistencePort.findAllowedPushNotification(pageable))
                .thenReturn(new SliceImpl<>(List.of(pushNotification), pageable, false));
        when(memberPersistencePort.findLocationById(1L))
                .thenReturn(Optional.empty());

        weatherPushNotificationScheduler.sendPushWeatherNotification();

        assertAll(
                () -> verify(weatherForecastInfoManager, never()).getForecasts(any(), any()),
                () -> verify(weatherPushNotificationMessageGenerator, never()).generateMessage(any())
        );
    }

    @DisplayName("푸시 알림을 전송할 수 있다.")
    @Test
    void sendPushWeatherNotification() {
        Pageable firstPageable = PageRequest.of(0, 10);
        Pageable secondPageable = PageRequest.of(1, 10);
        Pageable thirdPageable = PageRequest.of(2, 10);
        PushNotification pushNotification =
                new PushNotification(1L, 1L, "token1", PushNotificationTime.initial(), true);
        when(pushNotificationPersistencePort.findAllowedPushNotification(firstPageable))
                .thenReturn(new SliceImpl<>(generatePushNotifications(), firstPageable, true));
        when(pushNotificationPersistencePort.findAllowedPushNotification(secondPageable))
                .thenReturn(new SliceImpl<>(generatePushNotifications(), secondPageable, true));
        when(pushNotificationPersistencePort.findAllowedPushNotification(thirdPageable))
                .thenReturn(new SliceImpl<>(List.of(pushNotification), thirdPageable, false));
        when(memberPersistencePort.findLocationById(any()))
                .thenReturn(Optional.of(new Location(10.0, 20.0)));
        WeatherForecast weatherForecast = new WeatherForecast(10, 15);
        when(weatherForecastInfoManager.getForecasts(any(), any()))
                .thenReturn(weatherForecast);
        when(weatherPushNotificationMessageGenerator.generateMessage(weatherForecast))
                .thenReturn("푸시 알림 메시지");

        weatherPushNotificationScheduler.sendPushWeatherNotification();

        verify(webPushManager, times(3)).pushMessages(anyString(), anyList());
    }

    private List<PushNotification> generatePushNotifications() {
        return LongStream.range(1, 11)
                .mapToObj(index -> new PushNotification(
                        index, index, "token" + index, PushNotificationTime.initial(), true))
                .collect(Collectors.toUnmodifiableList());
    }
}
