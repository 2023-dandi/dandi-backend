package dandi.dandi.pushnotification.handler;

import static org.mockito.Mockito.verify;

import dandi.dandi.member.domain.NewMemberCreatedEvent;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PushNotificationEventHandlerTest {

    @Mock
    private PushNotificationRepository pushNotificationRepository;

    @InjectMocks
    private PushNotificationEventHandler pushNotificationEventHandler;

    @DisplayName("")
    @Test
    void a() {
        pushNotificationEventHandler.savePushNotificationByMember(new NewMemberCreatedEvent(1L));

        verify(pushNotificationRepository)
                .save(PushNotification.initial(1L));
    }
}