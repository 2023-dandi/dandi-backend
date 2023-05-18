package dandi.dandi.pushnotification.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.errormessage.application.port.out.ErrorMessageSender;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Location;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.application.port.out.webpush.WebPushManager;
import dandi.dandi.pushnotification.application.service.message.WeatherPushNotificationMessageGenerator;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationSource;
import dandi.dandi.pushnotification.domain.PushNotificationTime;
import dandi.dandi.weather.application.port.out.WeatherForecastInfoManager;
import dandi.dandi.weather.application.port.out.WeatherForecastResult;
import java.time.LocalDate;
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
    private final ErrorMessageSender errorMessageSender = Mockito.mock(ErrorMessageSender.class);

    private final WeatherPushNotificationScheduler weatherPushNotificationScheduler =
            new WeatherPushNotificationScheduler(pushNotificationPersistencePort, memberPersistencePort,
                    weatherForecastInfoManager, weatherPushNotificationMessageGenerator, webPushManager,
                    weatherPushTitle, errorMessageSender);

    @DisplayName("탈퇴한 회원에 대해 푸시 알림을 전송할수 없다.")
    @Test
    void sendPushWeatherNotification_withdrawnMember() {
        Pageable pageable = PageRequest.of(0, 10);
        PushNotification pushNotification =
                new PushNotification(1L, 1L, "token1", PushNotificationTime.initial(), true);
        PushNotification pushNotification2 =
                new PushNotification(1L, 2L, "token2", PushNotificationTime.initial(), true);
        when(pushNotificationPersistencePort.findAllowedPushNotification(pageable))
                .thenReturn(new SliceImpl<>(List.of(pushNotification, pushNotification2), pageable, false));
        when(memberPersistencePort.findLocationById(1L))
                .thenReturn(Optional.empty());
        Location location = new Location(10.0, 10.0);
        when(memberPersistencePort.findLocationById(2L))
                .thenReturn(Optional.of(location));
        WeatherForecastResult weatherForecastResult = WeatherForecastResult.ofSuccess(10, 10);
        when(weatherForecastInfoManager.getForecasts(LocalDate.now(), location))
                .thenReturn(weatherForecastResult);
        when(weatherPushNotificationMessageGenerator.generateMessage(weatherForecastResult))
                .thenReturn("body");

        weatherPushNotificationScheduler.sendPushWeatherNotification();

        assertAll(
                () -> verify(weatherForecastInfoManager, only()).getForecasts(any(), any()),
                () -> verify(weatherPushNotificationMessageGenerator, only()).generateMessage(any()),
                () -> verify(webPushManager).pushMessages(weatherPushTitle,
                        List.of(new PushNotificationSource("token2", "body")))
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
                .thenReturn(new SliceImpl<>(generateTenPushNotificationsOfTenMembers(), firstPageable, true));
        when(pushNotificationPersistencePort.findAllowedPushNotification(secondPageable))
                .thenReturn(new SliceImpl<>(generateTenPushNotificationsOfTenMembers(), secondPageable, true));
        when(pushNotificationPersistencePort.findAllowedPushNotification(thirdPageable))
                .thenReturn(new SliceImpl<>(List.of(pushNotification), thirdPageable, false));
        when(memberPersistencePort.findLocationById(any()))
                .thenReturn(Optional.of(new Location(10.0, 20.0)));
        WeatherForecastResult weatherForecastResult = WeatherForecastResult.ofSuccess(10, 15);
        when(weatherForecastInfoManager.getForecasts(any(), any()))
                .thenReturn(weatherForecastResult);
        when(weatherPushNotificationMessageGenerator.generateMessage(weatherForecastResult))
                .thenReturn("푸시 알림 메시지");

        weatherPushNotificationScheduler.sendPushWeatherNotification();

        verify(webPushManager, times(3)).pushMessages(anyString(), anyList());
    }

    @DisplayName("날씨 정보를 얻어오는데 실패하면 관리자에게 실패 메시지를 전송하고 푸시 알림을 전송한다.")
    @Test
    void sendPushWeatherNotification_FailureMessageToAdmin() {
        Pageable pageable = PageRequest.of(0, 10);
        PushNotification pushNotification =
                new PushNotification(1L, 1L, "token1", PushNotificationTime.initial(), true);
        when(pushNotificationPersistencePort.findAllowedPushNotification(pageable))
                .thenReturn(new SliceImpl<>(List.of(pushNotification), pageable, false));
        when(memberPersistencePort.findLocationById(pushNotification.getMemberId()))
                .thenReturn(Optional.of(new Location(10.0, 20.0)));
        WeatherForecastResult nonRetryableFailureResult = WeatherForecastResult.ofFailure("UNKNOWN_ERROR", false);
        when(weatherForecastInfoManager.getForecasts(any(), any()))
                .thenReturn(nonRetryableFailureResult);

        weatherPushNotificationScheduler.sendPushWeatherNotification();

        assertAll(
                () -> verify(errorMessageSender).sendMessage("회원(memberId : 1) 날씨 푸시 알림 전송 실패 / UNKNOWN_ERROR"),
                () -> verify(webPushManager).pushMessages(anyString(), anyList())
        );
    }

    @DisplayName("재시도 가능한 날씨 요청 실패에 대해 재시도 하여 푸시 알림을 전송할 수 있다.")
    @Test
    void sendPushWeatherNotification_RetrialOfFailure() {
        Pageable firstPageable = PageRequest.of(0, 10);
        when(pushNotificationPersistencePort.findAllowedPushNotification(firstPageable))
                .thenReturn(new SliceImpl<>(generateTenPushNotificationsOfTenMembers(), firstPageable, false));
        when(memberPersistencePort.findLocationById(any()))
                .thenReturn(Optional.of(new Location(10.0, 20.0)));
        mockWeatherForecastInfoManager();

        weatherPushNotificationScheduler.sendPushWeatherNotification();

        assertAll(
                () -> verify(weatherForecastInfoManager, times(15)).getForecasts(any(), any()),
                () -> verify(webPushManager, times(2)).pushMessages(anyString(), anyList()),
                () -> verify(errorMessageSender, times(3)).sendMessage(anyString())
        );
    }

    private void mockWeatherForecastInfoManager() {
        when(weatherForecastInfoManager.getForecasts(any(), any()))
                .thenReturn(WeatherForecastResult.ofSuccess(10, 50))
                .thenReturn(WeatherForecastResult.ofFailure("DB_ERROR", true))
                .thenReturn(WeatherForecastResult.ofSuccess(10, 50))
                .thenReturn(WeatherForecastResult.ofFailure("DB_ERROR", true))
                .thenReturn(WeatherForecastResult.ofSuccess(10, 50))
                .thenReturn(WeatherForecastResult.ofFailure("DB_ERROR", true))
                .thenReturn(WeatherForecastResult.ofSuccess(10, 50))
                .thenReturn(WeatherForecastResult.ofFailure("DB_ERROR", true))
                .thenReturn(WeatherForecastResult.ofFailure("UNKNOWN_ERROR", false))
                .thenReturn(WeatherForecastResult.ofFailure("DB_ERROR", true))
                // retrial Mocking From Here
                .thenReturn(WeatherForecastResult.ofSuccess(10, 50))
                .thenReturn(WeatherForecastResult.ofFailure("DB_ERROR", true))
                .thenReturn(WeatherForecastResult.ofSuccess(10, 50))
                .thenReturn(WeatherForecastResult.ofFailure("DB_ERROR", true))
                .thenReturn(WeatherForecastResult.ofSuccess(10, 50));
    }

    private List<PushNotification> generateTenPushNotificationsOfTenMembers() {
        return LongStream.range(1, 11)
                .mapToObj(index -> new PushNotification(
                        index, index, "token" + index, PushNotificationTime.initial(), true))
                .collect(Collectors.toUnmodifiableList());
    }
}
