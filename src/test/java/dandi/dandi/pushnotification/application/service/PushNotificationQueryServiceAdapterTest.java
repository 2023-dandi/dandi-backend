package dandi.dandi.pushnotification.application.service;

import static dandi.dandi.pushnotification.PushNotificationFixture.PUSH_NOTIFICATION_TOKEN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.pushnotification.application.port.in.PushNotificationResponse;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.domain.PushNotification;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PushNotificationQueryServiceAdapterTest {

    @Mock
    private PushNotificationPersistencePort pushNotificationPersistencePort;

    @InjectMocks
    private PushNotificationQueryServiceAdapter pushNotificationQueryServiceAdapter;

    @DisplayName("회원의 푸시 알림 정보를 반환한다.")
    @Test
    void findPushNotification() {
        Long memberId = 1L;
        when(pushNotificationPersistencePort.findPushNotificationByMemberId(memberId))
                .thenReturn(Optional.of(PushNotification.initial(memberId, PUSH_NOTIFICATION_TOKEN)));

        PushNotificationResponse pushNotificationResponse =
                pushNotificationQueryServiceAdapter.findPushNotification(memberId);

        assertAll(
                () -> assertThat(pushNotificationResponse.getPushNotificationTime()).isEqualTo(LocalTime.MIN),
                () -> assertThat(pushNotificationResponse.isAllowance()).isTrue()
        );
    }

    @DisplayName("회원의 푸시 알림이 존재하지 않는다면 반환하려고 하면 예외를 발생시킨다.")
    @Test
    void findPushNotification_NotFoundPushNotification() {
        Long notFoundPushNotificationMemberId = 1L;
        when(pushNotificationPersistencePort.findPushNotificationByMemberId(notFoundPushNotificationMemberId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> pushNotificationQueryServiceAdapter.findPushNotification(notFoundPushNotificationMemberId))
                .isInstanceOf(InternalServerException.class)
                .hasMessage("member(1)의 PushNotification이 존재하지 않습니다.");
    }

}
