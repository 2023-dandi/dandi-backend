package dandi.dandi.notification.application;

import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.PostLikeNotification;
import dandi.dandi.postlike.domain.PostLikedEvent;
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
public class PostLikeNotificationEventHandler {

    private final Logger logger = LoggerFactory.getLogger(PostLikeNotificationEventHandler.class);

    private final NotificationPersistencePort notificationPersistencePort;

    public PostLikeNotificationEventHandler(NotificationPersistencePort notificationPersistencePort) {
        this.notificationPersistencePort = notificationPersistencePort;
    }

    @TransactionalEventListener
    public void handlePostLikeNotificationEvent(PostLikedEvent postLikedEvent) {
        Notification notification = PostLikeNotification.initial(
                postLikedEvent.getTargetMemberId(), postLikedEvent.getPostId());
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
