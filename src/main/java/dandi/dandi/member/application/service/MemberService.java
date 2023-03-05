package dandi.dandi.member.application.service;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.port.in.MemberUseCase;
import dandi.dandi.member.application.port.in.dto.LocationUpdateRequest;
import dandi.dandi.member.application.port.in.dto.NicknameUpdateRequest;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.application.port.out.dto.MemberInfoResponse;
import dandi.dandi.member.application.port.out.dto.NicknameDuplicationCheckResponse;
import dandi.dandi.member.domain.Latitude;
import dandi.dandi.member.domain.Longitude;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.Nickname;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService implements MemberUseCase {

    private final MemberPersistencePort memberPersistencePort;

    public MemberService(MemberPersistencePort memberPersistencePort) {
        this.memberPersistencePort = memberPersistencePort;
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfoResponse findMemberInfo(Long memberId) {
        Member member = findMember(memberId);
        return new MemberInfoResponse(member);
    }

    @Override
    @Transactional
    public void updateNickname(Long memberId, NicknameUpdateRequest nicknameUpdateRequest) {
        Member member = findMember(memberId);
        Nickname nickname = Nickname.from(nicknameUpdateRequest.getNewNickname());
        memberPersistencePort.updateNickname(member.getId(), nickname.getValue());
    }

    @Override
    @Transactional
    public void updateLocation(Long memberId, LocationUpdateRequest locationUpdateRequest) {
        Member member = findMember(memberId);
        Latitude latitude = new Latitude(locationUpdateRequest.getLatitude());
        Longitude longitude = new Longitude(locationUpdateRequest.getLongitude());
        memberPersistencePort.updateLocation(member.getId(), latitude.getValue(), longitude.getValue());
    }

    private Member findMember(Long memberId) {
        return memberPersistencePort.findById(memberId)
                .orElseThrow(UnauthorizedException::notExistentMember);
    }

    @Override
    @Transactional(readOnly = true)
    public NicknameDuplicationCheckResponse checkDuplication(String nickname) {
        boolean duplicated = memberPersistencePort.existsMemberByNickname(nickname);
        return new NicknameDuplicationCheckResponse(duplicated);
    }
}
