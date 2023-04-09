package dandi.dandi.member.application.port.in;

public class MemberBlockCommand {

    private Long blockerMemberId;

    public MemberBlockCommand() {
    }

    public MemberBlockCommand(Long blockerMemberId) {
        this.blockerMemberId = blockerMemberId;
    }

    public Long getBlockerMemberId() {
        return blockerMemberId;
    }
}
