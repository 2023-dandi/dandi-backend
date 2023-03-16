package dandi.dandi.member.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberJpaEntity, Long> {

    @Query("SELECT m FROM MemberJpaEntity m WHERE m.oAuthId = :oAuthId")
    Optional<MemberJpaEntity> findByOAuthId(String oAuthId);

    @Query("SELECT m.nickname FROM MemberJpaEntity m WHERE m.id = :id")
    String findNicknameById(Long id);

    boolean existsMemberByNickname(String nickname);

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.profileImgUrl = :profileImageUrl WHERE m.id = :memberId")
    void updateProfileImageUrl(Long memberId, String profileImageUrl);

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.nickname = :nickname WHERE m.id = :memberId")
    void updateNickname(Long memberId, String nickname);

    boolean existsByNickname(String nickname);

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.latitude = :latitude, m.longitude = :longitude WHERE m.id = :memberId")
    void updateLocation(Long memberId, Double latitude, Double longitude);
}
