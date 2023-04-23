package dandi.dandi.member.domain;

import java.util.Objects;

public class NewMemberCreatedEvent {

    private final Long memberId;
    private final String pushNotificationToken;

    public NewMemberCreatedEvent(Long memberId, String pushNotificationToken) {
        this.memberId = memberId;
        this.pushNotificationToken = pushNotificationToken;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getPushNotificationToken() {
        return pushNotificationToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NewMemberCreatedEvent)) {
            return false;
        }
        NewMemberCreatedEvent that = (NewMemberCreatedEvent) o;
        return Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}
