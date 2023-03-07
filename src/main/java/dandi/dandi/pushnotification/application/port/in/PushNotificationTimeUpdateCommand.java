package dandi.dandi.pushnotification.application.port.in;

import dandi.dandi.common.validation.SelfValidating;
import java.time.LocalTime;
import javax.validation.constraints.NotNull;

public class PushNotificationTimeUpdateCommand extends SelfValidating<PushNotificationTimeUpdateCommand> {

    private static final String NULL_PUSH_NOTIFICATION_TIME_EXCEPTION_MESSAGE = "푸시 알림 변경 시간이 존재하지 않습니다.";
    private static final String INVALID_PUSH_NOTIFICATION_TIME_UNIT_EXCEPTION_MESSAGE = "푸시 알림 시간은 10분 단위입니다.";
    private static final int MINUTE_UNIT = 10;

    @NotNull
    private final LocalTime pushNotificationTime;

    public PushNotificationTimeUpdateCommand(LocalTime pushNotificationTime) {
        this.pushNotificationTime = pushNotificationTime;
        this.validateSelf(NULL_PUSH_NOTIFICATION_TIME_EXCEPTION_MESSAGE);
        validateZeroSecond();
        validateMinuteUnit();
    }

    private void validateZeroSecond() {
        if (pushNotificationTime.getSecond() != 0) {
            throw new IllegalArgumentException(INVALID_PUSH_NOTIFICATION_TIME_UNIT_EXCEPTION_MESSAGE);
        }
    }

    private void validateMinuteUnit() {
        if (pushNotificationTime.getMinute() % MINUTE_UNIT != 0) {
            throw new IllegalArgumentException(INVALID_PUSH_NOTIFICATION_TIME_UNIT_EXCEPTION_MESSAGE);
        }
    }

    public LocalTime getPushNotificationTime() {
        return pushNotificationTime;
    }
}
