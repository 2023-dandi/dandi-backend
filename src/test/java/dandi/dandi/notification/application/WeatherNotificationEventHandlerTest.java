package dandi.dandi.notification.application;


import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.WeatherNotification;
import dandi.dandi.pushnotification.domain.WeatherPushNotificationEvent;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeatherNotificationEventHandlerTest {

    @Mock
    private NotificationPersistencePort notificationPersistencePort;

    @InjectMocks
    private WeatherNotificationEventHandler weatherNotificationEventHandler;

    @DisplayName("날씨 알림을 저장할 수 있다.")
    @Test
    void handleWhetherNotificationEvent() {
        WeatherPushNotificationEvent weatherPushNotificationEvent =
                new WeatherPushNotificationEvent(MEMBER_ID, LocalDate.now());

        weatherNotificationEventHandler.handleWhetherNotificationEvent(weatherPushNotificationEvent);

        verify(notificationPersistencePort).save(any(WeatherNotification.class));
    }

}
