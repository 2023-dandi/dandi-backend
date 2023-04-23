package dandi.dandi.pushnotification;

import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationTime;
import java.time.LocalTime;

public class PushNotificationFixture {

    public static final String PUSH_NOTIFICATION_TOKEN = "asdasdsa";
    public static final PushNotification PUSH_NOTIFICATION =
            new PushNotification(1L, 1L, PUSH_NOTIFICATION_TOKEN, PushNotificationTime.from(LocalTime.MIDNIGHT), true);
}
