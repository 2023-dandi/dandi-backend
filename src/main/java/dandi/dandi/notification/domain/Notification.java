package dandi.dandi.notification.domain;

import static dandi.dandi.notification.domain.NotificationType.COMMENT;
import static dandi.dandi.notification.domain.NotificationType.POST_LIKE;

import java.time.LocalDateTime;

public class Notification {

    private final Long id;
    private final Long memberId;
    private final NotificationType type;
    private final LocalDateTime createdAt;
    private final Long postId;
    private final Long commentId;
    private final Long commentContent;

    public Notification(Long id, Long memberId, NotificationType type, LocalDateTime createdAt, Long postId,
                        Long commentId, Long commentContent) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.createdAt = createdAt;
        this.postId = postId;
        this.commentId = commentId;
        this.commentContent = commentContent;
    }

    public static Notification postLike(Long targetMemberId, Long postId) {
        return new Notification(null, targetMemberId, POST_LIKE, null, postId, null, null);
    }

    public static Notification postComment(Long targetMemberId, Long postId, Long commentId) {
        return new Notification(null, targetMemberId, COMMENT, null, postId, commentId, null);
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

    public Long getCommentId() {
        return commentId;
    }

    public Long getCommentContent() {
        return commentContent;
    }
}
