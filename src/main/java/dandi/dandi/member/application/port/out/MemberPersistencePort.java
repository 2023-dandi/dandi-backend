package dandi.dandi.member.application.port.out;

import dandi.dandi.member.domain.Member;
import java.util.Optional;

public interface MemberPersistencePort {

    Optional<Member> findById(Long memberId);

    Optional<Member> findByOAuthId(String oAuthId);

    boolean existsMemberByNickname(String nickname);

    void updateProfileImageUrl(Long memberId, String profileImageUrl);

    boolean existsByNickname(String nickname);

    Member save(Member member);

    void updateNickname(Long memberId, String newNickname);

    void updateLocation(Long memberId, Double latitude, Double longitude);
}
