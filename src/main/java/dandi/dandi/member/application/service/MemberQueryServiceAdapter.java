package dandi.dandi.member.application.service;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.MemberQueryServicePort;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
import dandi.dandi.post.application.port.out.MemberPostPersistencePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberQueryServiceAdapter implements MemberQueryServicePort {

    private final MemberPersistencePort memberPersistencePort;
    private final MemberPostPersistencePort memberPostPersistencePort;
    private final String imageAccessUrl;

    public MemberQueryServiceAdapter(MemberPersistencePort memberPersistencePort,
                                     MemberPostPersistencePort memberPostPersistencePort,
                                     @Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl) {
        this.memberPersistencePort = memberPersistencePort;
        this.memberPostPersistencePort = memberPostPersistencePort;
        this.imageAccessUrl = imageAccessUrl;
    }

    @Override
    public MemberInfoResponse findMemberInfo(Long memberId) {
        Member member = memberPersistencePort.findById(memberId)
                .orElseThrow(UnauthorizedException::notExistentMember);
        int postCount = memberPostPersistencePort.countPostByMemberId(memberId);
        return new MemberInfoResponse(member, postCount, imageAccessUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public NicknameDuplicationCheckResponse checkDuplication(Long memberId, String nickname) {
        boolean duplicated = memberPersistencePort.existsMemberByNicknameExceptMine(memberId, nickname);
        return new NicknameDuplicationCheckResponse(duplicated);
    }
}
