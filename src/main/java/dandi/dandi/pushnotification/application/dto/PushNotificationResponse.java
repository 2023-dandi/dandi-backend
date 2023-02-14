package dandi.dandi.pushnotification.application.dto;

import dandi.dandi.pushnotification.domain.PushNotification;
import java.time.LocalTime;

public class PushNotificationResponse {

    private LocalTime pushNotificationTime;
    private boolean allowance;

    public PushNotificationResponse() {
    }

    public PushNotificationResponse(PushNotification pushNotification) {
        this.pushNotificationTime = pushNotification.getPushNotificationTime();
        this.allowance = pushNotification.isAllowed();
    }

    public LocalTime getPushNotificationTime() {
        return pushNotificationTime;
    }

    public boolean isAllowance() {
        return allowance;
    }
}
