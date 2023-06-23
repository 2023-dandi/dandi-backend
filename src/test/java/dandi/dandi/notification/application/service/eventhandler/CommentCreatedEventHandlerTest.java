package dandi.dandi.notification.application.service.eventhandler;

import static dandi.dandi.comment.CommentFixture.COMMENT_ID;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import dandi.dandi.comment.domain.CommentCreatedEvent;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentCreatedEventHandlerTest {

    @Mock
    private NotificationPersistencePort notificationPersistencePort;

    @InjectMocks
    private CommentCreatedEventHandler commentCreatedEventHandler;

    @DisplayName("게시글에 새 댓글 작성 알림을 저장할 수 있다.")
    @Test
    void handleCommentCreatedNotificationEvent() {
        CommentCreatedEvent commentCreatedEvent = new CommentCreatedEvent(MEMBER_ID, POST_ID, COMMENT_ID);

        commentCreatedEventHandler.handleCommentCreatedNotificationEvent(commentCreatedEvent);

        verify(notificationPersistencePort).save(any(Notification.class));
    }
}
