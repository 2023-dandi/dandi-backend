package dandi.dandi.notification.adapter.persistence.convertor;

import static dandi.dandi.comment.CommentFixture.COMMENT_ID;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.notification.domain.NotificationType.COMMENT;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import dandi.dandi.comment.adapter.persistence.CommentJpaEntity;
import dandi.dandi.comment.adapter.persistence.CommentRepository;
import dandi.dandi.notification.adapter.persistence.NotificationJpaEntity;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.NotificationType;
import dandi.dandi.notification.domain.PostCommentNotification;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationConvertorsTest {

    private final PostLikeNotificationConvertor postLikeNotificationConvertor = new PostLikeNotificationConvertor();
    private final CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private final PostCommentNotificationConvertor postCommentNotificationConvertor =
            new PostCommentNotificationConvertor(commentRepository);
    private final WeatherNotificationConvertor weatherNotificationConvertor = new WeatherNotificationConvertor();
    private final NotificationConvertors notificationConvertors = new NotificationConvertors(
            postLikeNotificationConvertor, postCommentNotificationConvertor, weatherNotificationConvertor
    );

    @DisplayName("알림 유형에 따른 Notification 객체를 변환할 수 있다.(PostCommentNotification 제외")
    @ParameterizedTest
    @CsvSource({"POST_LIKE,PostLikeNotification", "WEATHER, WeatherNotification"})
    void convert_ExceptionPostCommentNotification(NotificationType notificationType,
                                                  String expectedNotificationClassName) {
        NotificationJpaEntity notificationJpaEntity =
                new NotificationJpaEntity(1L, MEMBER_ID, notificationType, true, null, null, null, LocalDateTime.now());

        Notification actual = notificationConvertors.convert(notificationJpaEntity);

        assertThat(actual.getClass().getSimpleName()).isEqualTo(expectedNotificationClassName);
    }

    @DisplayName("알림 유형에 따른 Notification 객체를 변환할 수 있다.(PostCommentNotification)")
    @Test
    void convert_PostCommentNotification() {
        NotificationJpaEntity notificationJpaEntity =
                new NotificationJpaEntity(1L, MEMBER_ID, COMMENT, true, POST_ID, COMMENT_ID, null, LocalDateTime.now());
        CommentJpaEntity commentJpaEntity = Mockito.mock(CommentJpaEntity.class);
        when(commentRepository.findById(COMMENT_ID))
                .thenReturn(Optional.of(commentJpaEntity));

        Notification actual = notificationConvertors.convert(notificationJpaEntity);

        assertThat(actual).isInstanceOf(PostCommentNotification.class);
    }
}
