package dandi.dandi.member.adapter.out.persistence.jpa;

import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.District;
import dandi.dandi.member.domain.Location;
import dandi.dandi.member.domain.Member;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
public class MemberPersistenceAdapter implements MemberPersistencePort {

    private final MemberRepository memberRepository;

    public MemberPersistenceAdapter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member save(Member member) {
        return memberRepository.save(MemberEntity.fromMember(member))
                .toMember();
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberEntity::toMember);
    }

    @Override
    public Optional<Member> findByOAuthId(String oAuthId) {
        return memberRepository.findByOAuthId(oAuthId)
                .map(MemberEntity::toMember);
    }

    @Override
    public boolean existsMemberByNickname(String nickname) {
        return memberRepository.existsMemberByNickname(nickname);
    }

    @Override
    public boolean existsMemberByNicknameExceptMine(Long memberId, String nickname) {
        return memberRepository.existsByNicknameAndIdIsNot(nickname, memberId);
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
    public void updateLocation(Long memberId, Location location) {
        District district = location.getDistrict();
        memberRepository.updateLocation(memberId, location.getLatitude(), location.getLongitude(),
                district.getFirst(), district.getSecond(), district.getThird());
    }

    @Override
    public boolean existsById(Long id) {
        return memberRepository.existsById(id);
    }

    @Override
    public Slice<Member> findPushNotificationAllowingMember(Pageable pageable) {
        Slice<MemberEntity> memberJpaEntities = memberRepository.findPushNotificationAllowingMember(pageable);
        List<Member> pushNotificationAllowingMembers = memberJpaEntities.stream()
                .map(MemberEntity::toMember)
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(pushNotificationAllowingMembers, pageable, memberJpaEntities.hasNext());
    }

    @Override
    public Optional<Location> findLocationById(Long id) {
        return memberRepository.findLocationById(id)
                .map(LocationEntity::toLocation);
    }

    @Override
    public Optional<Long> findIdByNickname(String nickname) {
        return memberRepository.findIdByNickname(nickname);
    }
}
