package dandi.dandi.member.application.port.in;

import dandi.dandi.member.web.dto.out.MemberInfoResponse;
import dandi.dandi.member.web.dto.out.NicknameDuplicationCheckResponse;

public interface MemberUseCase {

    MemberInfoResponse findMemberInfo(Long memberId);

    void updateNickname(Long memberId, NicknameUpdateCommand nicknameUpdateCommand);

    void updateLocation(Long memberId, LocationUpdateCommand locationUpdateCommand);

    NicknameDuplicationCheckResponse checkDuplication(String nickname);
}
