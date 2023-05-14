package dandi.dandi.pushnotification.application.handler;

import static dandi.dandi.pushnotification.PushNotificationFixture.PUSH_NOTIFICATION_TOKEN;
import static org.mockito.Mockito.verify;

import dandi.dandi.member.domain.NewMemberCreatedEvent;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.domain.PushNotification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PushNotificationEventHandlerTest {

    @Mock
    private PushNotificationPersistencePort notificationPersistencePort;

    @InjectMocks
    private PushNotificationEventHandler pushNotificationEventHandler;

    @DisplayName("새 회원 가입 이벤트를 받아, 새 회원에 대한 푸시 알림을 생성한다.")
    @Test
    void savePushNotificationByMember() {
        NewMemberCreatedEvent newMemberCreatedEvent = new NewMemberCreatedEvent(1L, PUSH_NOTIFICATION_TOKEN);

        pushNotificationEventHandler.savePushNotificationByMember(newMemberCreatedEvent);

        verify(notificationPersistencePort).save(PushNotification.initial(1L, PUSH_NOTIFICATION_TOKEN));
    }
}
