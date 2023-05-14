package dandi.dandi.pushnotification.adapter.out.webpush.fcm;

import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import dandi.dandi.pushnotification.application.port.out.webpush.WebPushManager;
import dandi.dandi.pushnotification.application.service.PushNotificationSource;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class FcmWebPushManager implements WebPushManager {

    private final FirebaseMessaging firebaseMessaging;

    public FcmWebPushManager(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void pushMessages(String title, List<PushNotificationSource> pushNotificationSources) {
        List<Message> messages = pushNotificationSources.stream()
                .map(notification -> generateMessage(title, notification))
                .collect(Collectors.toUnmodifiableList());
        firebaseMessaging.sendAllAsync(messages);
    }

    private Message generateMessage(String title, PushNotificationSource weatherNotification) {
        ApsAlert apsAlert = ApsAlert.builder()
                .setTitle(title)
                .setBody(weatherNotification.getBody())
                .build();
        Aps aps = Aps.builder()
                .setAlert(apsAlert)
                .setContentAvailable(true)
                .build();
        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(aps)
                .build();
        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setToken(weatherNotification.getToken())
                .build();
    }
}
