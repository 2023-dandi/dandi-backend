package dandi.dandi.notification.domain;

import java.time.LocalDateTime;

public class PostNotification extends Notification {

    private final Long postId;

    public PostNotification(Long id, Long memberId, NotificationType type, Long postId, LocalDateTime createdAt) {
        super(id, memberId, type, createdAt);
        this.postId = postId;
    }

    public static PostNotification initial(Long memberId, Long postId, NotificationType notificationType) {
        return new PostNotification(null, memberId, notificationType, postId, null);
    }

    @Override
    public Long getContentId() {
        return postId;
    }
}
