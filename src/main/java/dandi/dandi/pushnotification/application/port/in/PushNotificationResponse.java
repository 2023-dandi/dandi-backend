package dandi.dandi.pushnotification.application.port.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import dandi.dandi.pushnotification.domain.PushNotification;
import io.swagger.v3.oas.annotations.media.Schema;
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
