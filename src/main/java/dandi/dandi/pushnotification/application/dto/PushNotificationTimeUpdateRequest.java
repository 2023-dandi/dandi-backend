package dandi.dandi.pushnotification.application.dto;

import java.time.LocalTime;

public class PushNotificationTimeUpdateRequest {

    private LocalTime newPushNotificationTime;

    public PushNotificationTimeUpdateRequest() {
    }

    public PushNotificationTimeUpdateRequest(LocalTime newPushNotificationTime) {
        this.newPushNotificationTime = newPushNotificationTime;
    }

    public LocalTime getNewPushNotificationTime() {
        return newPushNotificationTime;
    }
}
