package dandi.dandi.pushnotification.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.pushnotification.application.port.in.PushNotificationAllowanceUpdateCommand;
import dandi.dandi.pushnotification.application.port.in.PushNotificationResponse;
import dandi.dandi.pushnotification.application.port.in.PushNotificationTimeUpdateCommand;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.application.sevice.PushNotificationService;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationTime;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PushNotificationServiceTest {

    private static final PushNotification PUSH_NOTIFICATION =
            new PushNotification(1L, 1L, PushNotificationTime.from(LocalTime.MIDNIGHT), true);

    @Mock
    private PushNotificationPersistencePort pushNotificationPersistencePort;

    @InjectMocks
    private PushNotificationService pushNotificationService;

    @DisplayName("회원의 푸시 알림 정보를 반환한다.")
    @Test
    void findPushNotification() {
        Long memberId = 1L;
        when(pushNotificationPersistencePort.findPushNotificationByMemberId(memberId))
                .thenReturn(Optional.of(PushNotification.initial(memberId)));

        PushNotificationResponse pushNotificationResponse = pushNotificationService.findPushNotification(memberId);

        assertAll(
                () -> assertThat(pushNotificationResponse.getPushNotificationTime()).isEqualTo(LocalTime.MIN),
                () -> assertThat(pushNotificationResponse.isAllowance()).isFalse()
        );
    }

    @DisplayName("회원의 푸시 알림이 존재하지 않는다면 반환하려고 하면 예외를 발생시킨다.")
    @Test
    void findPushNotification_NotFoundPushNotification() {
        Long notFoundPushNotificationMemberId = 1L;
        when(pushNotificationPersistencePort.findPushNotificationByMemberId(notFoundPushNotificationMemberId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> pushNotificationService.findPushNotification(notFoundPushNotificationMemberId))
                .isInstanceOf(InternalServerException.class)
                .hasMessage("member(1)의 PushNotification이 존재하지 않습니다.");
    }

    @DisplayName("회원의 푸시 알림 시간을 변경할 수 있다.")
    @Test
    void updatePushNotificationTime() {
        LocalTime newPushNotificationTime = LocalTime.of(10, 20);
        when(pushNotificationPersistencePort.findPushNotificationByMemberId(PUSH_NOTIFICATION.getMemberId()))
                .thenReturn(Optional.of(PUSH_NOTIFICATION));

        pushNotificationService.updatePushNotificationTime(
                PUSH_NOTIFICATION.getMemberId(), new PushNotificationTimeUpdateCommand(newPushNotificationTime));

        verify(pushNotificationPersistencePort)
                .updatePushNotificationTime(PUSH_NOTIFICATION.getId(), newPushNotificationTime);
    }

    @DisplayName("회원의 푸시 알림 허용 여부를 변경할 수 있다.")
    @Test
    void updatePushNotificationAllowance() {
        when(pushNotificationPersistencePort.findPushNotificationByMemberId(PUSH_NOTIFICATION.getMemberId()))
                .thenReturn(Optional.of(PUSH_NOTIFICATION));

        pushNotificationService.updatePushNotificationAllowance(
                PUSH_NOTIFICATION.getMemberId(), new PushNotificationAllowanceUpdateCommand(true));

        verify(pushNotificationPersistencePort).updatePushNotificationAllowance(PUSH_NOTIFICATION.getId(), true);
    }
}
