package dandi.dandi.member.application.port.in;

public class MemberBlockCommand {

    private Long memberId;

    public MemberBlockCommand() {
    }

    public MemberBlockCommand(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
