package dandi.dandi.notification.domain;

import java.time.LocalDateTime;

public abstract class PostNotification extends Notification {

    private final Long postId;

    protected PostNotification(Long id, Long memberId, NotificationType type, Long postId, LocalDateTime createdAt) {
        super(id, memberId, type, createdAt);
        this.postId = postId;
    }

    @Override
    public final Long getContentId() {
        return postId;
    }
}
