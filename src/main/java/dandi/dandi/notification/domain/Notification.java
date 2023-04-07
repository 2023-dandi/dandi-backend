package dandi.dandi.notification.domain;

import static dandi.dandi.notification.domain.NotificationType.POST_LIKE;

import java.time.LocalDateTime;

public class Notification {

    private final Long id;
    private final Long memberId;
    private final NotificationType type;
    private final LocalDateTime createdAt;
    private final Long postId;

    public Notification(Long id, Long memberId, NotificationType type, LocalDateTime createdAt, Long postId) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.createdAt = createdAt;
        this.postId = postId;
    }

    public static Notification postLike(Long targetMemberId, Long postId) {
        return new Notification(null, targetMemberId, POST_LIKE, null, postId);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public NotificationType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getPostId() {
        return postId;
    }
}
