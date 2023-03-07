package dandi.dandi.pushnotification.application.port.in;

import dandi.dandi.common.validation.SelfValidating;
import javax.validation.constraints.NotNull;

public class PushNotificationAllowanceUpdateCommand extends SelfValidating<PushNotificationAllowanceUpdateCommand> {

    private static final String NULL_PUSH_NOTIFICATION_ALLOWANCE_UPDATE_VALUE_EXCEPTION_MESSAGE =
            "푸시 알림 허용 여부가 입력되지 않았습니다.";

    @NotNull
    private final Boolean allowed;

    public PushNotificationAllowanceUpdateCommand(Boolean allowed) {
        this.allowed = allowed;
        this.validateSelf(NULL_PUSH_NOTIFICATION_ALLOWANCE_UPDATE_VALUE_EXCEPTION_MESSAGE);
    }

    public Boolean isAllowed() {
        return allowed;
    }
}
