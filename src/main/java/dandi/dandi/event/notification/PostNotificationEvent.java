package dandi.dandi.event.notification;

import dandi.dandi.notification.domain.NotificationType;

public class PostNotificationEvent {

    private final Long targetMemberId;
    private final Long postId;
    private final Long commentId;
    private final NotificationType notificationType;

    public PostNotificationEvent(Long targetMemberId, Long postId, Long commentId, NotificationType notificationType) {
        this.targetMemberId = targetMemberId;
        this.postId = postId;
        this.commentId = commentId;
        this.notificationType = notificationType;
    }

    public static PostNotificationEvent postLike(Long targetMemberId, Long postId) {
        return new PostNotificationEvent(targetMemberId, postId, null, NotificationType.POST_LIKE);
    }

    public static PostNotificationEvent comment(Long targetMemberId, Long postId, Long commentId) {
        return new PostNotificationEvent(targetMemberId, postId, commentId, NotificationType.COMMENT);
    }

    public Long getTargetMemberId() {
        return targetMemberId;
    }

    public Long getPostId() {
        return postId;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }
}
