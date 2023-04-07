package dandi.dandi.notification.domain;

import static dandi.dandi.notification.domain.NotificationType.POST_LIKE;

public class PostLikeNotification extends PostNotification {

    public PostLikeNotification(Long id, Long memberId, NotificationType type, Long postId) {
        super(id, memberId, type, postId);
    }

    public static PostLikeNotification initial(Long memberId, Long postId) {
        return new PostLikeNotification(null, memberId, POST_LIKE, postId);
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
