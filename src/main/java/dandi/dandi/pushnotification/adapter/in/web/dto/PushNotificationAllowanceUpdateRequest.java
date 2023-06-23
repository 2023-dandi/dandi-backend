package dandi.dandi.pushnotification.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dandi.dandi.pushnotification.application.port.in.PushNotificationAllowanceUpdateCommand;

public class PushNotificationAllowanceUpdateRequest {

    @JsonFormat
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
