package dandi.dandi.notification.application;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.postlike.domain.PostLikedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostLikeNotificationEventHandlerTest {

    @Mock
    private NotificationPersistencePort notificationPersistencePort;

    @InjectMocks
    private PostLikeNotificationEventHandler postLikeNotificationEventHandler;

    @DisplayName("게시글 좋아요 알림을 저장할 수 있다.")
    @Test
    void handlePostLikeNotificationEvent() {
        PostLikedEvent postLikedEvent = new PostLikedEvent(MEMBER_ID, POST_ID);

        postLikeNotificationEventHandler.handlePostLikeNotificationEvent(postLikedEvent);

        verify(notificationPersistencePort).save(any());
    }
}
