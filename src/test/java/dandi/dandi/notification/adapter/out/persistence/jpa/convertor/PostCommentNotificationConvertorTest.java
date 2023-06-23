package dandi.dandi.notification.adapter.out.persistence.jpa.convertor;

import static dandi.dandi.comment.CommentFixture.COMMENT_CONTENT;
import static dandi.dandi.notification.domain.NotificationType.COMMENT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import dandi.dandi.comment.adapter.out.persistence.jpa.CommentJpaEntity;
import dandi.dandi.comment.adapter.out.persistence.jpa.CommentRepository;
import dandi.dandi.notification.adapter.out.persistence.jpa.NotificationJpaEntity;
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
class PostCommentNotificationConvertorTest {

    private final CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private final PostCommentNotificationConvertor postLikeNotificationConvertor =
            new PostCommentNotificationConvertor(commentRepository);

    @DisplayName("게시글 댓글 타입 알림을 변환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"COMMENT, true", "POST_LIKE, false", "WEATHER, false"})
    void canConvert(NotificationType type, boolean expected) {
        NotificationJpaEntity notificationJpaEntity = new NotificationJpaEntity(
                1L, null, type, true, null, null, null, null);

        boolean actual = postLikeNotificationConvertor.canConvert(notificationJpaEntity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("게시글 댓글 타입 알림을 Notification 객체로 변환할 수 있다.")
    @Test
    void convert() {
        Long id = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        NotificationJpaEntity notificationJpaEntity = new NotificationJpaEntity(
                id, null, COMMENT, true, postId, commentId, null, createdAt);
        CommentJpaEntity commentJpaEntity = Mockito.mock(CommentJpaEntity.class);
        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(commentJpaEntity));
        when(commentJpaEntity.getContent())
                .thenReturn(COMMENT_CONTENT);

        Notification actual = postLikeNotificationConvertor.convert(notificationJpaEntity);

        assertAll(
                () -> assertThat(actual).isInstanceOf(PostCommentNotification.class),
                () -> assertThat(actual.getId()).isEqualTo(id),
                () -> assertThat(actual.getPostId()).isEqualTo(postId),
                () -> assertThat(actual.getCommentId()).isEqualTo(commentId),
                () -> assertThat(actual.getCommentContent()).isEqualTo(COMMENT_CONTENT),
                () -> assertThat(actual.getCreatedAt()).isEqualTo(createdAt.toLocalDate())
        );
    }

}
