package dandi.dandi.pushnotification.domain;

import java.time.LocalTime;
import java.util.Objects;

public class PushNotification {

    private Long id;
    private Long memberId;
    private PushNotificationTime pushNotificationTime;
    private boolean allowance;

    public PushNotification(Long id, Long memberId, PushNotificationTime pushNotificationTime, boolean allowance) {
        this.id = id;
        this.memberId = memberId;
        this.pushNotificationTime = pushNotificationTime;
        this.allowance = allowance;
    }

    public static PushNotification initial(Long memberId) {
        return new PushNotification(null, memberId, PushNotificationTime.initial(), false);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalTime getPushNotificationTime() {
        return pushNotificationTime.getValue();
    }

    public boolean isAllowed() {
        return allowance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotification)) {
            return false;
        }
        PushNotification that = (PushNotification) o;
        return allowance == that.allowance && Objects.equals(id, that.id) && Objects.equals(memberId,
                that.memberId) && Objects.equals(pushNotificationTime, that.pushNotificationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, pushNotificationTime, allowance);
    }
}
