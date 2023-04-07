package dandi.dandi.notification.domain;

import static dandi.dandi.notification.domain.NotificationType.COMMENT;
import static dandi.dandi.notification.domain.NotificationType.POST_LIKE;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Notification {

    private final Long id;
    private final Long memberId;
    private final NotificationType type;
    private final LocalDateTime createdAt;
    private final Long postId;
    private final Long commentId;
    private final Long commentContent;
    private final LocalDate weatherDate;

    public Notification(Long id, Long memberId, NotificationType type, LocalDateTime createdAt, Long postId,
                        Long commentId, Long commentContent, LocalDate weatherDate) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.createdAt = createdAt;
        this.postId = postId;
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.weatherDate = weatherDate;
    }

    public static Notification postLike(Long targetMemberId, Long postId) {
        return new Notification(null, targetMemberId, POST_LIKE, null, postId, null, null, null);
    }

    public static Notification postComment(Long targetMemberId, Long postId, Long commentId) {
        return new Notification(null, targetMemberId, COMMENT, null, postId, commentId, null, null);
    }

    public static Notification whether(Long targetMemberId, LocalDate whetherDate) {
        return new Notification(null, targetMemberId, null, null, null, null, null, whetherDate);
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

    public LocalDate getWeatherDate() {
        return weatherDate;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", postId=" + postId +
                ", commentId=" + commentId +
                ", commentContent=" + commentContent +
                '}';
    }
}
