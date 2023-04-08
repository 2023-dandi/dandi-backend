package dandi.dandi.notification.domain;

import static dandi.dandi.notification.domain.NotificationType.POST_LIKE;

import java.time.LocalDate;

public class PostLikeNotification extends PostNotification {

    public PostLikeNotification(Long id, Long memberId, NotificationType type, LocalDate createdAt, Long postId) {
        super(id, memberId, type, createdAt, postId);
    }

    public static PostLikeNotification initial(Long memberId, Long postId) {
        return new PostLikeNotification(null, memberId, POST_LIKE, null, postId);
    }

    @Override
    public final Long getCommentId() {
        return null;
    }

    @Override
    public final String getCommentContent() {
        return null;
    }
}
