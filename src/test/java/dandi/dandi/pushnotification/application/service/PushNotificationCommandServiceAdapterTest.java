package dandi.dandi.pushnotification.application.service;

import static dandi.dandi.pushnotification.PushNotificationFixture.PUSH_NOTIFICATION;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.pushnotification.application.port.in.PushNotificationAllowanceUpdateCommand;
import dandi.dandi.pushnotification.application.port.in.PushNotificationTimeUpdateCommand;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PushNotificationCommandServiceAdapterTest {

    @Mock
    private PushNotificationPersistencePort pushNotificationPersistencePort;

    @InjectMocks
    private PushNotificationCommandServiceAdapter pushNotificationCommandServiceAdapter;

    @DisplayName("회원의 푸시 알림 시간을 변경할 수 있다.")
    @Test
    void updatePushNotificationTime() {
        LocalTime newPushNotificationTime = LocalTime.of(10, 20);
        when(pushNotificationPersistencePort.findPushNotificationByMemberId(PUSH_NOTIFICATION.getMemberId()))
                .thenReturn(Optional.of(PUSH_NOTIFICATION));

        pushNotificationCommandServiceAdapter.updatePushNotificationTime(
                PUSH_NOTIFICATION.getMemberId(), new PushNotificationTimeUpdateCommand(newPushNotificationTime));

        verify(pushNotificationPersistencePort)
                .updatePushNotificationTime(PUSH_NOTIFICATION.getId(), newPushNotificationTime);
    }

    @DisplayName("회원의 푸시 알림 허용 여부를 변경할 수 있다.")
    @Test
    void updatePushNotificationAllowance() {
        when(pushNotificationPersistencePort.findPushNotificationByMemberId(PUSH_NOTIFICATION.getMemberId()))
                .thenReturn(Optional.of(PUSH_NOTIFICATION));

        pushNotificationCommandServiceAdapter.updatePushNotificationAllowance(
                PUSH_NOTIFICATION.getMemberId(), new PushNotificationAllowanceUpdateCommand(true));

        verify(pushNotificationPersistencePort).updatePushNotificationAllowance(PUSH_NOTIFICATION.getId(), true);
    }
}
