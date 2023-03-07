package dandi.dandi.pushnotification.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dandi.dandi.pushnotification.application.port.in.PushNotificationAllowanceUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;

public class PushNotificationAllowanceUpdateRequest {

    @JsonFormat
    @Schema(example = "true")
    private Boolean allowed;

    public PushNotificationAllowanceUpdateRequest() {
    }

    public PushNotificationAllowanceUpdateRequest(boolean allowed) {
        this.allowed = allowed;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public PushNotificationAllowanceUpdateCommand toCommand() {
        return new PushNotificationAllowanceUpdateCommand(allowed);
    }
}
