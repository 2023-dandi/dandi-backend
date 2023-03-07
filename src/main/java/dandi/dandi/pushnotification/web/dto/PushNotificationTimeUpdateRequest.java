package dandi.dandi.pushnotification.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dandi.dandi.pushnotification.application.port.in.PushNotificationTimeUpdateCommand;
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

    public PushNotificationTimeUpdateCommand toCommand() {
        return new PushNotificationTimeUpdateCommand(newPushNotificationTime);
    }
}
