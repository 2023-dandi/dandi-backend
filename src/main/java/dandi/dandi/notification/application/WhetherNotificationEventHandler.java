package dandi.dandi.notification.application;

import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.WeatherNotification;
import dandi.dandi.pushnotification.domain.WeatherPushNotificationEvent;
import org.springframework.transaction.event.TransactionalEventListener;

public class WhetherNotificationEventHandler extends NotificationEventHandler {

    public WhetherNotificationEventHandler(NotificationPersistencePort notificationPersistencePort) {
        super(notificationPersistencePort);
    }

    @TransactionalEventListener
    public void handleCommentCreatedNotificationEvent(WeatherPushNotificationEvent weatherPushNotificationEvent) {
        Notification notification = WeatherNotification.initial(
                weatherPushNotificationEvent.getMemberId(), weatherPushNotificationEvent.getWeatherDate());
        saveNotification(notification);
    }
}
