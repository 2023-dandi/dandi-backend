package dandi.dandi.member.application.service;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.port.in.LocationUpdateCommand;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.MemberUseCase;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.port.in.NicknameUpdateCommand;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
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
    public void updateNickname(Long memberId, NicknameUpdateCommand nicknameUpdateCommand) {
        Member member = findMember(memberId);
        memberPersistencePort.updateNickname(member.getId(), nicknameUpdateCommand.getNickname());
    }

    @Override
    @Transactional
    public void updateLocation(Long memberId, LocationUpdateCommand locationUpdateCommand) {
        Member member = findMember(memberId);
        memberPersistencePort.updateLocation(member.getId(),
                locationUpdateCommand.getLatitude(), locationUpdateCommand.getLongitude());
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
