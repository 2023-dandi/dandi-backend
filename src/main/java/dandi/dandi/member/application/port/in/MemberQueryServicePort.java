package dandi.dandi.member.application.port.in;

public interface MemberQueryServicePort {

    MemberInfoResponse findMemberInfo(Long memberId);

    NicknameDuplicationCheckResponse checkDuplication(Long memberId, String nickname);
}
