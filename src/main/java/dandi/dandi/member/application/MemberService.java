package dandi.dandi.member.application;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.dto.MemberInfoResponse;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse findMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(UnauthorizedException::notExistentMember);
        return new MemberInfoResponse(member);
    }
}
