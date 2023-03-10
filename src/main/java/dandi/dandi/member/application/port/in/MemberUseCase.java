package dandi.dandi.member.application.port.in;

public interface MemberUseCase {

    MemberInfoResponse findMemberInfo(Long memberId);

    void updateNickname(Long memberId, NicknameUpdateCommand nicknameUpdateCommand);

    void updateLocation(Long memberId, LocationUpdateCommand locationUpdateCommand);

    NicknameDuplicationCheckResponse checkDuplication(String nickname);
}
