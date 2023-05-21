package dandi.dandi.member.application.port.out;

import dandi.dandi.member.domain.Location;
import dandi.dandi.member.domain.Member;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemberPersistencePort {

    Optional<Member> findById(Long memberId);

    Optional<Member> findByOAuthId(String oAuthId);

    boolean existsMemberByNicknameExceptMine(Long memberId, String nickname);

    void updateProfileImageUrl(Long memberId, String profileImageUrl);

    Member save(Member member);

    void updateNickname(Long memberId, String newNickname);

    void updateLocation(Long memberId, Double latitude, Double longitude);

    boolean existsMemberByNickname(String nickname);

    boolean existsById(Long id);

    Slice<Member> findPushNotificationAllowingMember(Pageable pageable);

    Optional<Location> findLocationById(Long id);

    Optional<Long> findIdByNickname(String nickname);
}
