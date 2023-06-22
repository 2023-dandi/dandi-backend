package dandi.dandi.member.application.port.in;

public interface MemberBlockUseCasePort {

    void blockMember(Long memberId, MemberBlockCommand memberBlockCommand);
}
