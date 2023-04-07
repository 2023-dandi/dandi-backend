package dandi.dandi.event.notification;

import dandi.dandi.notification.domain.NotificationType;

public class PostNotificationEvent {

    private final Long targetMemberId;
    private final Long postId;
    private final NotificationType notificationType;

    public PostNotificationEvent(Long targetMemberId, Long postId, NotificationType notificationType) {
        this.targetMemberId = targetMemberId;
        this.postId = postId;
        this.notificationType = notificationType;
    }

    public static PostNotificationEvent postLike(Long targetMemberId, Long postId) {
        return new PostNotificationEvent(targetMemberId, postId, NotificationType.POST_LIKE);
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
