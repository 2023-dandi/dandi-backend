package dandi.dandi.member.application.port.in;

public interface MemberUseCaseServicePort {

    void updateNickname(Long memberId, NicknameUpdateCommand nicknameUpdateCommand);

    void updateLocation(Long memberId, LocationUpdateCommand locationUpdateCommand);

    void blockMember(Long memberId, MemberBlockCommand memberBlockCommand);
}
