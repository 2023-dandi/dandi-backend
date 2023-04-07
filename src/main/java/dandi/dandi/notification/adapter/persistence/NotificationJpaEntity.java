package dandi.dandi.notification.adapter.persistence;

import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.NotificationType;
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

    private Long postId;

    private Long commentId;

    @CreatedDate
    private LocalDateTime createdAt;

    protected NotificationJpaEntity() {
    }

    public NotificationJpaEntity(Long id, Long memberId, NotificationType notificationType, Long postId, Long commentId,
                                 LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.notificationType = notificationType;
        this.postId = postId;
        this.commentId = commentId;
        this.createdAt = createdAt;
    }

    public static NotificationJpaEntity fromNotification(Notification notification) {
        return new NotificationJpaEntity(
                notification.getId(),
                notification.getMemberId(),
                notification.getType(),
                notification.getPostId(),
                notification.getCommentId(),
                notification.getCreatedAt()
        );
    }
}
