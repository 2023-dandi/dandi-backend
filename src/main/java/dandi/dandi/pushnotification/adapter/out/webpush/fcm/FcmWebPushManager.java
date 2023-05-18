package dandi.dandi.pushnotification.adapter.out.webpush.fcm;

import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import dandi.dandi.pushnotification.application.port.out.webpush.WebPushManager;
import dandi.dandi.pushnotification.domain.PushNotificationSource;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class FcmWebPushManager implements WebPushManager {

    private static final int MAX_SIZE_ABLE_TO_SEND_IN_ONCE = 100;

    private final FirebaseMessaging firebaseMessaging;

    public FcmWebPushManager(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void pushMessages(String title, List<PushNotificationSource> pushNotificationSources) {
        List<Message> messages = convertToMessages(title, pushNotificationSources);
        if (messages.size() > MAX_SIZE_ABLE_TO_SEND_IN_ONCE) {
            pushBySplitting(messages);
            return;
        }
        firebaseMessaging.sendAllAsync(messages);
    }

    private List<Message> convertToMessages(String title, List<PushNotificationSource> pushNotificationSources) {
        return pushNotificationSources.stream()
                .map(notification -> generateMessage(title, notification))
                .collect(Collectors.toUnmodifiableList());
    }

    private void pushBySplitting(List<Message> messages) {
        int index;
        for (index = 0; index + MAX_SIZE_ABLE_TO_SEND_IN_ONCE < messages.size();
             index += MAX_SIZE_ABLE_TO_SEND_IN_ONCE) {
            List<Message> temp = messages.subList(index, index + MAX_SIZE_ABLE_TO_SEND_IN_ONCE);
            firebaseMessaging.sendAllAsync(temp);
        }
        List<Message> remaining = messages.subList(index, messages.size());
        firebaseMessaging.sendAllAsync(remaining);
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
