package dandi.dandi.notification.adapter.out.persistence.jpa.convertor;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.comment.adapter.out.persistence.jpa.CommentRepository;
import dandi.dandi.notification.adapter.out.persistence.jpa.NotificationJpaEntity;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.NotificationType;
import dandi.dandi.notification.domain.PostCommentNotification;
import org.springframework.stereotype.Component;

@Component
public class PostCommentNotificationConvertor implements NotificationConvertor {

    private final CommentRepository commentRepository;

    public PostCommentNotificationConvertor(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public boolean canConvert(NotificationJpaEntity notificationJpaEntity) {
        return notificationJpaEntity.hasNotificationType(NotificationType.COMMENT);
    }

    @Override
    public Notification convert(NotificationJpaEntity notificationJpaEntity) {
        Long commentId = notificationJpaEntity.getCommentId();
        String commentContent = commentRepository.findById(commentId)
                .orElseThrow(() -> InternalServerException.notificationCommentNotFound(commentId))
                .getContent();
        return new PostCommentNotification(
                notificationJpaEntity.getId(),
                notificationJpaEntity.getMemberId(),
                notificationJpaEntity.getNotificationType(),
                notificationJpaEntity.getCreatedAt().toLocalDate(),
                notificationJpaEntity.isChecked(),
                notificationJpaEntity.getPostId(),
                commentId,
                commentContent
        );
    }
}
