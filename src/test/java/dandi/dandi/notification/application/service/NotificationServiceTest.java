package dandi.dandi.notification.application.service;

import static dandi.dandi.comment.CommentFixture.COMMENT_CONTENT;
import static dandi.dandi.comment.CommentFixture.COMMENT_ID;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.notification.domain.NotificationType.COMMENT;
import static dandi.dandi.notification.domain.NotificationType.POST_LIKE;
import static dandi.dandi.notification.domain.NotificationType.WEATHER;
import static dandi.dandi.post.PostFixture.POST_ID;
import static dandi.dandi.utils.PaginationUtils.CREATED_AT_DESC_TEST_SIZE_PAGEABLE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import dandi.dandi.notification.application.port.in.NotificationResponse;
import dandi.dandi.notification.application.port.in.NotificationResponses;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.PostCommentNotification;
import dandi.dandi.notification.domain.PostLikeNotification;
import dandi.dandi.notification.domain.WeatherNotification;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationPersistencePort notificationPersistencePort;

    @InjectMocks
    private NotificationService notificationService;

    @DisplayName("사용자의 알림들을 조회할 수 있다.")
    @Test
    void getNotifications() {
        List<Notification> notifications = List.of(
                new WeatherNotification(4L, MEMBER_ID, WEATHER, false, LocalDate.now()),
                new PostCommentNotification(3L, MEMBER_ID, COMMENT, LocalDate.now(), true, POST_ID,
                        COMMENT_ID, COMMENT_CONTENT),
                new PostLikeNotification(2L, MEMBER_ID, POST_LIKE, LocalDate.now(), true, POST_ID),
                new PostLikeNotification(1L, MEMBER_ID, POST_LIKE, LocalDate.now(), true, POST_ID));
        when(notificationPersistencePort.findByMemberId(MEMBER_ID, CREATED_AT_DESC_TEST_SIZE_PAGEABLE))
                .thenReturn(new SliceImpl<>(notifications, CREATED_AT_DESC_TEST_SIZE_PAGEABLE, false));

        NotificationResponses actual =
                notificationService.getNotifications(MEMBER_ID, CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        List<NotificationResponse> notificationResponses = actual.getNotifications();
        assertThat(notificationResponses).hasSize(4);
    }
}
