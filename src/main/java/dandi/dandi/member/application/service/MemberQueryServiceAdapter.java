package dandi.dandi.member.application.service;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.image.aspect.ImageUrlInclusion;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.MemberQueryServicePort;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.application.port.out.MemberPostPersistencePort;
import dandi.dandi.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberQueryServiceAdapter implements MemberQueryServicePort {

    private final MemberPersistencePort memberPersistencePort;
    private final MemberPostPersistencePort memberPostPersistencePort;

    public MemberQueryServiceAdapter(MemberPersistencePort memberPersistencePort,
                                     MemberPostPersistencePort memberPostPersistencePort) {
        this.memberPersistencePort = memberPersistencePort;
        this.memberPostPersistencePort = memberPostPersistencePort;
    }

    @Override
    @ImageUrlInclusion
    public MemberInfoResponse findMemberInfo(Long memberId) {
        Member member = memberPersistencePort.findById(memberId)
                .orElseThrow(UnauthorizedException::notExistentMember);
        int postCount = memberPostPersistencePort.countPostByMemberId(memberId);
        return new MemberInfoResponse(member, postCount);
    }

    @Override
    public NicknameDuplicationCheckResponse checkDuplication(Long memberId, String nickname) {
        boolean duplicated = memberPersistencePort.existsMemberByNicknameExceptMine(memberId, nickname);
        return new NicknameDuplicationCheckResponse(duplicated);
    }
}
