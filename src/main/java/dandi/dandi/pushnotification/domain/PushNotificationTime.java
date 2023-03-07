package dandi.dandi.pushnotification.domain;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PushNotificationTime {

    private static final int MINUTE_UNIT = 10;

    private static final Map<LocalTime, PushNotificationTime> CACHE = new HashMap<>();
    private static final PushNotificationTime INITIAL = PushNotificationTime.from(LocalTime.MIN);

    public static PushNotificationTime initial() {
        return INITIAL;
    }

    private LocalTime value;

    private PushNotificationTime(LocalTime value) {
        validateZeroSecond(value);
        this.value = value;
    }

    private void validateZeroSecond(LocalTime value) {
        if (value.getSecond() != 0) {
            throw new IllegalArgumentException("푸시 알림 시간은 10분 단위입니다.");
        }
    }

    public static PushNotificationTime from(LocalTime value) {
        validateMinuteUnit(value);
        return CACHE.computeIfAbsent(value, ignored -> new PushNotificationTime(value));
    }

    private static void validateMinuteUnit(LocalTime value) {
        if (value.getMinute() % MINUTE_UNIT != 0) {
            throw new IllegalArgumentException("푸시 알림 시간은 10분 단위입니다.");
        }
    }

    public LocalTime getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationTime)) {
            return false;
        }
        PushNotificationTime that = (PushNotificationTime) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
