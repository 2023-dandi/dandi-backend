package dandi.dandi.notification.adapter.out.persistence.jpa.convertor;

import static dandi.dandi.notification.domain.NotificationType.POST_LIKE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.notification.adapter.out.persistence.jpa.NotificationJpaEntity;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.NotificationType;
import dandi.dandi.notification.domain.PostLikeNotification;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PostLikeNotificationConvertorTest {

    private final PostLikeNotificationConvertor postLikeNotificationConvertor = new PostLikeNotificationConvertor();

    @DisplayName("게시글 좋아요 타입 알림을 변환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"POST_LIKE, true", "COMMENT, false", "WEATHER, false"})
    void canConvert(NotificationType type, boolean expected) {
        NotificationJpaEntity notificationJpaEntity = new NotificationJpaEntity(
                1L, null, type, true, null, null, null, null);

        boolean actual = postLikeNotificationConvertor.canConvert(notificationJpaEntity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("게시글 좋아요 타입 알림을 Notification 객체로 변환할 수 있다.")
    @Test
    void convert() {
        Long id = 1L;
        Long postId = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        NotificationJpaEntity notificationJpaEntity = new NotificationJpaEntity(
                id, null, POST_LIKE, true, postId, null, null, createdAt);

        Notification actual = postLikeNotificationConvertor.convert(notificationJpaEntity);

        assertAll(
                () -> assertThat(actual).isInstanceOf(PostLikeNotification.class),
                () -> assertThat(actual.getId()).isEqualTo(id),
                () -> assertThat(actual.getPostId()).isEqualTo(postId),
                () -> assertThat(actual.getCreatedAt()).isEqualTo(createdAt.toLocalDate())
        );
    }
}
