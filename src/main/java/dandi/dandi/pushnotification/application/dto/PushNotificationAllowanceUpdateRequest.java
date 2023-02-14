package dandi.dandi.pushnotification.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PushNotificationAllowanceUpdateRequest {

    @JsonFormat
    private Boolean allowance;

    public PushNotificationAllowanceUpdateRequest() {
    }

    public PushNotificationAllowanceUpdateRequest(boolean allowance) {
        this.allowance = allowance;
    }

    public boolean isAllowed() {
        return allowance;
    }
}
