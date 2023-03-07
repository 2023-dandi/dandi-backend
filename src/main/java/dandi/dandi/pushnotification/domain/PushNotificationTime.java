package dandi.dandi.pushnotification.domain;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PushNotificationTime {

    private static final Map<LocalTime, PushNotificationTime> CACHE = new HashMap<>();
    private static final PushNotificationTime INITIAL = PushNotificationTime.from(LocalTime.MIN);

    public static PushNotificationTime initial() {
        return INITIAL;
    }

    private final LocalTime value;

    private PushNotificationTime(LocalTime value) {
        this.value = value;
    }

    public static PushNotificationTime from(LocalTime value) {
        return CACHE.computeIfAbsent(value, ignored -> new PushNotificationTime(value));
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
