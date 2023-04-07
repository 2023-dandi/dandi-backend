package dandi.dandi.notification.application;

import dandi.dandi.comment.domain.CommentCreatedEvent;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Async("asyncExecutor")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CommentCreatedEventHandler {

    private final Logger logger = LoggerFactory.getLogger(CommentCreatedEventHandler.class);

    private final NotificationPersistencePort notificationPersistencePort;

    public CommentCreatedEventHandler(NotificationPersistencePort notificationPersistencePort) {
        this.notificationPersistencePort = notificationPersistencePort;
    }

    @TransactionalEventListener
    public void handleCommentCreatedNotificationEvent(CommentCreatedEvent commentCreatedEvent) {
        Notification notification = Notification.postComment(commentCreatedEvent.getTargetMemberId(),
                commentCreatedEvent.getPostId(), commentCreatedEvent.getCommentId());
        saveNotification(notification);
    }

    private void saveNotification(Notification notification) {
        try {
            notificationPersistencePort.save(notification);
        } catch (Exception e) {
            logger.info("알림 저장 실패 : " + notification.toString());
        }
    }
}
