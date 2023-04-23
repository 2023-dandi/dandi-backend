package dandi.dandi.pushnotification.domain;

import java.time.LocalTime;
import java.util.Objects;

public class PushNotification {

    private final Long id;
    private final Long memberId;
    private final String token;
    private final PushNotificationTime pushNotificationTime;
    private final boolean allowance;

    public PushNotification(Long id, Long memberId, String token, PushNotificationTime pushNotificationTime,
                            boolean allowance) {
        this.id = id;
        this.memberId = memberId;
        this.token = token;
        this.pushNotificationTime = pushNotificationTime;
        this.allowance = allowance;
    }

    public static PushNotification initial(Long memberId, String token) {
        return new PushNotification(null, memberId, token, PushNotificationTime.initial(), true);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getToken() {
        return token;
    }

    public LocalTime getPushNotificationTime() {
        return pushNotificationTime.getValue();
    }

    public boolean isAllowed() {
        return allowance;
    }

    public boolean hasToken(String pushNotificationToken) {
        return token.equals(pushNotificationToken);
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
