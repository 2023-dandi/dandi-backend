package dandi.dandi.member.domain;

import java.util.Objects;

public class NewMemberCreatedEvent {

    private final Long memberId;

    public NewMemberCreatedEvent(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
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
