package dandi.dandi.pushnotification.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.pushnotification.application.dto.PushNotificationResponse;
import dandi.dandi.pushnotification.application.dto.PushNotificationTimeUpdateRequest;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationRepository;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PushNotificationServiceTest {

    @Mock
    private PushNotificationRepository pushNotificationRepository;

    @InjectMocks
    private PushNotificationService pushNotificationService;

    @DisplayName("회원의 푸시 알림 정보를 반환한다.")
    @Test
    void findPushNotification() {
        Long memberId = 1L;
        when(pushNotificationRepository.findPushNotificationByMemberId(memberId))
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
        when(pushNotificationRepository.findPushNotificationByMemberId(notFoundPushNotificationMemberId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> pushNotificationService.findPushNotification(notFoundPushNotificationMemberId))
                .isInstanceOf(InternalServerException.class)
                .hasMessage("member(1)의 PushNotification이 존재하지 않습니다.");
    }

    @DisplayName("회원의 푸시 알림 시간을 변경할 수 있다.")
    @Test
    void a() {
        Long memberId = 1L;
        PushNotification pushNotification = Mockito.mock(PushNotification.class);
        LocalTime newPushNotificationTime = LocalTime.of(10, 20);
        when(pushNotificationRepository.findPushNotificationByMemberId(memberId))
                .thenReturn(Optional.of(pushNotification));

        pushNotificationService.updatePushNotificationTime(
                memberId, new PushNotificationTimeUpdateRequest(newPushNotificationTime));

        verify(pushNotification).updatePushNotificationTime(newPushNotificationTime);
    }
}
