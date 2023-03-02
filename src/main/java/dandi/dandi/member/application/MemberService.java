package dandi.dandi.member.application;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.dto.LocationUpdateRequest;
import dandi.dandi.member.application.dto.MemberInfoResponse;
import dandi.dandi.member.application.dto.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.dto.NicknameUpdateRequest;
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
        Member member = findMember(memberId);
        return new MemberInfoResponse(member);
    }

    @Transactional
    public void updateNickname(Long memberId, NicknameUpdateRequest nicknameUpdateRequest) {
        Member member = findMember(memberId);
        member.updateNickname(nicknameUpdateRequest.getNewNickname());
    }

    @Transactional
    public void updateLocation(Long memberId, LocationUpdateRequest locationUpdateRequest) {
        Member member = findMember(memberId);
        member.updateLocation(locationUpdateRequest.getLatitude(), locationUpdateRequest.getLongitude());
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(UnauthorizedException::notExistentMember);
    }

    @Transactional(readOnly = true)
    public NicknameDuplicationCheckResponse checkDuplication(String nickname) {
        boolean duplicated = memberRepository.existsMemberByNicknameValue(nickname);
        return new NicknameDuplicationCheckResponse(duplicated);
    }
}
