package dandi.dandi.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.oAuthId = :oAuthId")
    Optional<Member> findByOAuthId(String oAuthId);

    boolean existsMemberByNicknameValue(String nickname);

    @Modifying
    @Query("UPDATE Member m SET m.profileImgUrl = :profileImageUrl WHERE m.id = :memberId")
    void updateProfileImageUrl(Long memberId, String profileImageUrl);

    boolean existsByNicknameValue(String nickname);
}
