package dandi.dandi.pushnotification.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public class PushNotificationTimeUpdateRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
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
