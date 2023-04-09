package dandi.dandi.member.application.service;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.member.application.port.in.LocationUpdateCommand;
import dandi.dandi.member.application.port.in.MemberBlockCommand;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.MemberUseCase;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.port.in.NicknameUpdateCommand;
import dandi.dandi.member.application.port.out.MemberBlockPersistencePort;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService implements MemberUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final MemberBlockPersistencePort memberBlockPersistencePort;
    private final String imageAccessUrl;

    public MemberService(MemberPersistencePort memberPersistencePort,
                         MemberBlockPersistencePort memberBlockPersistencePort,
                         @Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl) {
        this.memberPersistencePort = memberPersistencePort;
        this.memberBlockPersistencePort = memberBlockPersistencePort;
        this.imageAccessUrl = imageAccessUrl;
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfoResponse findMemberInfo(Long memberId) {
        Member member = findMember(memberId);
        return new MemberInfoResponse(member, imageAccessUrl);
    }

    @Override
    @Transactional
    public void updateNickname(Long memberId, NicknameUpdateCommand nicknameUpdateCommand) {
        Member member = findMember(memberId);
        try {
            memberPersistencePort.updateNickname(member.getId(), nicknameUpdateCommand.getNickname());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
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
    public NicknameDuplicationCheckResponse checkDuplication(Long memberId, String nickname) {
        boolean duplicated = memberPersistencePort.existsMemberByNicknameExceptMine(memberId, nickname);
        return new NicknameDuplicationCheckResponse(duplicated);
    }

    @Override
    @Transactional
    public void blockMember(Long memberId, MemberBlockCommand memberBlockCommand) {
        Long blockedMemberId = memberBlockCommand.getMemberId();
        validateMemberExistence(blockedMemberId);
        validateAlreadyBlocked(memberId, blockedMemberId);
        memberBlockPersistencePort.saveMemberBlockOf(memberId, blockedMemberId);
    }

    private void validateMemberExistence(Long blockedMemberId) {
        if (!memberPersistencePort.existsById(blockedMemberId)) {
            throw NotFoundException.member();
        }
    }

    private void validateAlreadyBlocked(Long memberId, Long blockedMemberId) {
        if (memberBlockPersistencePort.existsByBlockingMemberIdAndBlockedMemberId(memberId, blockedMemberId)) {
            throw new IllegalStateException("이미 차단한 사용자입니다.");
        }
    }
}
