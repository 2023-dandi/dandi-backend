package dandi.dandi.pushnotification.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

public class PushNotificationTimeUpdateRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    @Schema(example = "20:30")
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
