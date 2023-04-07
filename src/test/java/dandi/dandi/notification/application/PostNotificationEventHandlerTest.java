package dandi.dandi.notification.application;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import dandi.dandi.event.notification.PostNotificationEvent;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostNotificationEventHandlerTest {

    @Mock
    private NotificationPersistencePort notificationPersistencePort;

    @InjectMocks
    private PostNotificationEventHandler postNotificationEventHandler;

    @DisplayName("게시글 알림을 저장할 수 있다.")
    @Test
    void handlePostNotificationEvent_SelfPublishingEvent() {
        PostNotificationEvent postNotificationEvent = PostNotificationEvent.postLike(MEMBER_ID, POST_ID);

        postNotificationEventHandler.handlePostNotificationEvent(postNotificationEvent);

        verify(notificationPersistencePort).save(any());
    }
}
