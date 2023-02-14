package dandi.dandi.pushnotification.domain;

import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PushNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_notification_id")
    private Long id;

    private Long memberId;

    @Embedded
    private PushNotificationTime pushNotificationTime;

    private boolean allowance;

    private PushNotification() {
    }

    private PushNotification(Long id, Long memberId, PushNotificationTime pushNotificationTime, boolean allowance) {
        this.id = id;
        this.memberId = memberId;
        this.pushNotificationTime = pushNotificationTime;
        this.allowance = allowance;
    }

    public static PushNotification initial(Long memberId) {
        return new PushNotification(null, memberId, PushNotificationTime.initial(), false);
    }

    public void updatePushNotificationTime(LocalTime pushNotificationTime) {
        this.pushNotificationTime = PushNotificationTime.from(pushNotificationTime);
    }

    public void updateAllowance(boolean allowance) {
        this.allowance = allowance;
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
