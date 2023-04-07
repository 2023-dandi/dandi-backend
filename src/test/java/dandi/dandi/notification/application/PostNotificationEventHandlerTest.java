package dandi.dandi.notification.application;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import dandi.dandi.event.notification.PostNotificationEvent;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostNotificationEventHandlerTest {

    @Mock
    private NotificationPersistencePort notificationPersistencePort;

    @InjectMocks
    private PostNotificationEventHandler postNotificationEventHandler;

    @DisplayName("알려야 할 알림 이벤트는 저장하고 아닌 이벤트는 저장하지 않을 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, 0", "2, 1"})
    void handlePostNotificationEvent_SelfPublishingEvent(Long publisherId, int expected) {
        PostNotificationEvent postNotificationEvent = PostNotificationEvent.postLike(MEMBER_ID, publisherId, POST_ID);

        postNotificationEventHandler.handlePostNotificationEvent(postNotificationEvent);

        verify(notificationPersistencePort, times(expected)).save(any());
    }
}
