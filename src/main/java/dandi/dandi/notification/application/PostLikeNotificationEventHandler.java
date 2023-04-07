package dandi.dandi.notification.application;

import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.postlike.domain.PostLikedEvent;
import org.springframework.transaction.event.TransactionalEventListener;

public class PostLikeNotificationEventHandler extends NotificationEventHandler {

    public PostLikeNotificationEventHandler(NotificationPersistencePort notificationPersistencePort) {
        super(notificationPersistencePort);
    }

    @TransactionalEventListener
    public void handlePostLikeNotificationEvent(PostLikedEvent postLikedEvent) {
        Notification notification = Notification.postLike(
                postLikedEvent.getTargetMemberId(), postLikedEvent.getPostId());
        saveNotification(notification);
    }
}
