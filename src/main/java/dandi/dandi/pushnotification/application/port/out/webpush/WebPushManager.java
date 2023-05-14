package dandi.dandi.pushnotification.application.port.out.webpush;

import dandi.dandi.pushnotification.application.service.PushNotificationSource;
import java.util.List;

public interface WebPushManager {

    void pushMessages(String title, List<PushNotificationSource> pushNotificationSources);
}
