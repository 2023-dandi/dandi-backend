package dandi.dandi.member.application.port.in;

import dandi.dandi.member.application.port.in.dto.LocationUpdateRequest;
import dandi.dandi.member.application.port.in.dto.NicknameUpdateRequest;
import dandi.dandi.member.application.port.out.dto.MemberInfoResponse;
import dandi.dandi.member.application.port.out.dto.NicknameDuplicationCheckResponse;

public interface MemberUseCase {

    MemberInfoResponse findMemberInfo(Long memberId);

    void updateNickname(Long memberId, NicknameUpdateRequest nicknameUpdateRequest);

    void updateLocation(Long memberId, LocationUpdateRequest locationUpdateRequest);

    NicknameDuplicationCheckResponse checkDuplication(String nickname);
}
