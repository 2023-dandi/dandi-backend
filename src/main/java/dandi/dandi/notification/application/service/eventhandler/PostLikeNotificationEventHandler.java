package dandi.dandi.notification.application.service.eventhandler;

import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.PostLikeNotification;
import dandi.dandi.postlike.domain.PostLikedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PostLikeNotificationEventHandler extends NotificationEventHandler {

    public PostLikeNotificationEventHandler(NotificationPersistencePort notificationPersistencePort) {
        super(notificationPersistencePort);
    }

    @TransactionalEventListener
    public void handlePostLikeNotificationEvent(PostLikedEvent postLikedEvent) {
        Notification notification =
                PostLikeNotification.initial(postLikedEvent.getTargetMemberId(), postLikedEvent.getPostId());
        saveNotification(notification);
    }
}
