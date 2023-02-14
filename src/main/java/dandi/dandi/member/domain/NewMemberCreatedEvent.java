package dandi.dandi.member.domain;

public class NewMemberCreatedEvent {

    private final Long memberId;

    public NewMemberCreatedEvent(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
