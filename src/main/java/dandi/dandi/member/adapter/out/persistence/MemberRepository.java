package dandi.dandi.member.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberJpaEntity, Long> {

    @Query("SELECT m FROM MemberJpaEntity m WHERE m.oAuthId = :oAuthId")
    Optional<MemberJpaEntity> findByOAuthId(String oAuthId);

    boolean existsMemberByNickname(String nickname);

    boolean existsByNicknameAndIdIsNot(String nickname, Long memberId);

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.profileImgUrl = :profileImageUrl WHERE m.id = :memberId")
    void updateProfileImageUrl(Long memberId, String profileImageUrl);

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.nickname = :nickname WHERE m.id = :memberId")
    void updateNickname(Long memberId, String nickname);

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.locationJpaEntity.longitude = :longitude,"
            + " m.locationJpaEntity.latitude = :latitude WHERE m.id = :memberId")
    void updateLocation(Long memberId, Double latitude, Double longitude);

    @Query("SELECT m FROM MemberJpaEntity m INNER JOIN PushNotificationJpaEntity pn ON m.id = pn.memberId "
            + "WHERE pn.allowance = true")
    Slice<MemberJpaEntity> findPushNotificationAllowingMember(Pageable pageable);

    @Query("SELECT m.locationJpaEntity FROM MemberJpaEntity m WHERE m.id = :id")
    Optional<LocationJpaEntity> findLocationById(Long id);
}
