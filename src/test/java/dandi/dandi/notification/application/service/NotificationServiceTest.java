package dandi.dandi.notification.application.service;

import static dandi.dandi.comment.CommentFixture.COMMENT_CONTENT;
import static dandi.dandi.comment.CommentFixture.COMMENT_ID;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.notification.domain.NotificationType.COMMENT;
import static dandi.dandi.notification.domain.NotificationType.POST_LIKE;
import static dandi.dandi.notification.domain.NotificationType.WEATHER;
import static dandi.dandi.post.PostFixture.POST_ID;
import static dandi.dandi.utils.PaginationUtils.CREATED_AT_DESC_TEST_SIZE_PAGEABLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.notification.application.port.in.NotificationResponse;
import dandi.dandi.notification.application.port.in.NotificationResponses;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.PostCommentNotification;
import dandi.dandi.notification.domain.PostLikeNotification;
import dandi.dandi.notification.domain.WeatherNotification;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    private static final Long NOTIFICATION_ID = 1L;

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

    @DisplayName("존재하지 않는 알림의 id로 확인 여부를 true로 변경하려하면 예외를 발생시킨다.")
    @Test
    void checkNotification_NotFound() {
        when(notificationPersistencePort.findById(NOTIFICATION_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationService.checkNotification(MEMBER_ID, NOTIFICATION_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.notification().getMessage());
    }

    @DisplayName("다른 사용자의 알림 확인 여부를 변경하려하면 예외를 발생시킨다.")
    @Test
    void checkNotification_Forbidden() {
        WeatherNotification notification =
                new WeatherNotification(NOTIFICATION_ID, MEMBER_ID, WEATHER, false, LocalDate.now());
        when(notificationPersistencePort.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));
        Long anotherMemberId = 2L;

        assertThatThrownBy(() -> notificationService.checkNotification(anotherMemberId, NOTIFICATION_ID))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ForbiddenException.notificationCheckModification().getMessage());
    }
}
