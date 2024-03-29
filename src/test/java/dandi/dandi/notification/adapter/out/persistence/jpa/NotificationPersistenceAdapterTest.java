package dandi.dandi.notification.adapter.out.persistence.jpa;

import static dandi.dandi.comment.CommentFixture.COMMENT_CONTENT;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static dandi.dandi.utils.PaginationUtils.CREATED_AT_DESC_TEST_SIZE_PAGEABLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.comment.adapter.out.persistence.jpa.CommentPersistenceAdapter;
import dandi.dandi.comment.domain.Comment;
import dandi.dandi.common.PersistenceAdapterTest;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.PostCommentNotification;
import dandi.dandi.notification.domain.PostLikeNotification;
import dandi.dandi.notification.domain.WeatherNotification;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

class NotificationPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private NotificationPersistenceAdapter notificationPersistenceAdapter;

    @Autowired
    private CommentPersistenceAdapter commentPersistenceAdapter;

    @DisplayName("알림을 저장할 수 있다.")
    @Test
    void save() {
        Notification notification = PostLikeNotification.initial(MEMBER_ID, POST_ID);

        assertThatCode(() -> notificationPersistenceAdapter.save(notification))
                .doesNotThrowAnyException();
    }

    @DisplayName("id에 해당하는 알림을 찾을 수 있다.")
    @Test
    void findById() {
        Notification notification = PostLikeNotification.initial(MEMBER_ID, POST_ID);
        notificationPersistenceAdapter.save(notification);

        Optional<Notification> actual = notificationPersistenceAdapter.findById(1L);

        assertThat(actual).isPresent();
    }

    @DisplayName("id에 해당하는 알림의 확인 여부를 true로 변경할 수 있다.")
    @Test
    void updateCheckTrue() {
        Notification notification = PostLikeNotification.initial(MEMBER_ID, POST_ID);
        notificationPersistenceAdapter.save(notification);
        boolean checkBeforeUpdate = notification.isChecked();

        notificationPersistenceAdapter.updateCheckTrue(1L);

        entityManager.clear();
        boolean checkAfterUpdate = notificationPersistenceAdapter.findById(1L)
                .get()
                .isChecked();
        assertAll(
                () -> assertThat(checkBeforeUpdate).isFalse(),
                () -> assertThat(checkAfterUpdate).isTrue()
        );
    }

    @DisplayName("사용자의 알림을 찾을 수 있다.")
    @Test
    void findByMemberId() {
        Long commentId = commentPersistenceAdapter.save(Comment.initial(COMMENT_CONTENT), POST_ID, MEMBER_ID);
        notificationPersistenceAdapter.save(PostCommentNotification.initial(MEMBER_ID, POST_ID, commentId));
        notificationPersistenceAdapter.save(PostLikeNotification.initial(MEMBER_ID, POST_ID));
        notificationPersistenceAdapter.save(PostLikeNotification.initial(MEMBER_ID, POST_ID));
        notificationPersistenceAdapter.save(WeatherNotification.initial(MEMBER_ID, LocalDate.now()));

        Slice<Notification> notifications =
                notificationPersistenceAdapter.findByMemberId(MEMBER_ID, CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        assertThat(notifications).hasSize(4);
    }
}
