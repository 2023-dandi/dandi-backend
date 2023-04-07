package dandi.dandi.notification.domain;

import java.time.LocalDateTime;

public abstract class Notification {

    private final Long id;
    private final Long memberId;
    private final NotificationType type;
    private final LocalDateTime createdAt;

    protected Notification(Long id, Long memberId, NotificationType type, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.createdAt = createdAt;
    }

    public final Long getId() {
        return id;
    }

    public final Long getMemberId() {
        return memberId;
    }

    public final NotificationType getType() {
        return type;
    }

    public final LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public abstract Long getContentId();

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", contentId=" + getContentId() +
                '}';
    }
}
