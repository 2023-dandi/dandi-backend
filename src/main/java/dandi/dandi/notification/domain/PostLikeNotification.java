package dandi.dandi.notification.domain;

import java.time.LocalDateTime;

public class PostLikeNotification extends PostNotification {

    public PostLikeNotification(Long id, Long memberId, NotificationType type, Long postId, LocalDateTime createdAt) {
        super(id, memberId, type, postId, createdAt);
    }

    public static PostLikeNotification initial(Long memberId, Long postId) {
        return new PostLikeNotification(null, memberId, NotificationType.POST_LIKE, postId, null);
    }
}
