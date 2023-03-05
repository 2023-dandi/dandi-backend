package dandi.dandi.member.adapter.out.persistence;

import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MemberPersistenceAdapter implements MemberPersistencePort {

    private final MemberRepository memberRepository;

    public MemberPersistenceAdapter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member save(Member member) {
        return memberRepository.save(MemberJpaEntity.fromMember(member))
                .toMember();
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberJpaEntity::toMember);
    }

    @Override
    public Optional<Member> findByOAuthId(String oAuthId) {
        return memberRepository.findByOAuthId(oAuthId)
                .map(MemberJpaEntity::toMember);
    }

    @Override
    public boolean existsMemberByNickname(String nickname) {
        return memberRepository.existsMemberByNickname(nickname);
    }

    @Override
    public void updateProfileImageUrl(Long memberId, String profileImageUrl) {
        memberRepository.updateProfileImageUrl(memberId, profileImageUrl);
    }

    @Override
    public void updateNickname(Long memberId, String newNickname) {
        memberRepository.updateNickname(memberId, newNickname);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public void updateLocation(Long memberId, Double latitude, Double longitude) {
        memberRepository.updateLocation(memberId, latitude, longitude);
    }
}
