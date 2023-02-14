package dandi.dandi.pushnotification.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
}
