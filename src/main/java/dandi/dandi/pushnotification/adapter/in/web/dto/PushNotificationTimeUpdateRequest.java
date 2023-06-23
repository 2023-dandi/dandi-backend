package dandi.dandi.pushnotification.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dandi.dandi.pushnotification.application.port.in.PushNotificationTimeUpdateCommand;
import java.time.LocalTime;

public class PushNotificationTimeUpdateRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime pushNotificationTime;

    public PushNotificationTimeUpdateRequest() {
    }

    public PushNotificationTimeUpdateRequest(LocalTime pushNotificationTime) {
        this.pushNotificationTime = pushNotificationTime;
    }

    public PushNotificationTimeUpdateCommand toCommand() {
        return new PushNotificationTimeUpdateCommand(pushNotificationTime);
    }
}
