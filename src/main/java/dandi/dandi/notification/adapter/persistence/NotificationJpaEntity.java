package dandi.dandi.notification.adapter.persistence;

import static dandi.dandi.notification.domain.NotificationType.COMMENT;
import static dandi.dandi.notification.domain.NotificationType.POST_LIKE;
import static dandi.dandi.notification.domain.NotificationType.WEATHER;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.NotificationType;
import dandi.dandi.notification.domain.PostCommentNotification;
import dandi.dandi.notification.domain.PostLikeNotification;
import dandi.dandi.notification.domain.WeatherNotification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
public class NotificationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private Long memberId;

    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    private boolean checked;

    private Long postId;

    private Long commentId;

    private LocalDate weatherDate;

    @CreatedDate
    private LocalDateTime createdAt;

    protected NotificationJpaEntity() {
    }

    public NotificationJpaEntity(Long id, Long memberId, NotificationType notificationType, boolean checked,
                                 Long postId,
                                 Long commentId, LocalDate weatherDate, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.notificationType = notificationType;
        this.checked = checked;
        this.postId = postId;
        this.commentId = commentId;
        this.weatherDate = weatherDate;
        this.createdAt = createdAt;
    }

    public static NotificationJpaEntity fromNotification(Notification notification) {
        return new NotificationJpaEntity(
                notification.getId(),
                notification.getMemberId(),
                notification.getType(),
                notification.isChecked(),
                notification.getPostId(),
                notification.getCommentId(),
                notification.getWeatherDate(),
                null
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public boolean isChecked() {
        return checked;
    }

    public Long getPostId() {
        return postId;
    }

    public LocalDate getWeatherDate() {
        return weatherDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getCommentId() {
        return commentId;
    }

    public PostCommentNotification toPostCommentNotification(String commentContent) {
        return new PostCommentNotification(
                id, memberId, COMMENT, createdAt.toLocalDate(), checked, postId, commentId, commentContent);
    }

    public boolean hasNotificationType(NotificationType notificationType) {
        return this.notificationType == notificationType;
    }

    public Notification toNotification() {
        validateNotPostCommentNotification();
        if (notificationType == POST_LIKE) {
            return new PostLikeNotification(id, memberId, POST_LIKE, createdAt.toLocalDate(), checked, postId);
        }
        return new WeatherNotification(id, memberId, WEATHER, createdAt.toLocalDate(), checked, weatherDate);
    }

    private void validateNotPostCommentNotification() {
        if (notificationType == COMMENT) {
            throw InternalServerException.commentNotificationConvert(id);
        }
    }
}
