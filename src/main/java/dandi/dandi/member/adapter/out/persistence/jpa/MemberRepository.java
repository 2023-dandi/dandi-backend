package dandi.dandi.member.adapter.out.persistence.jpa;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    @Query("SELECT m FROM MemberEntity m WHERE m.oAuthId = :oAuthId")
    Optional<MemberEntity> findByOAuthId(String oAuthId);

    boolean existsMemberByNickname(String nickname);

    boolean existsByNicknameAndIdIsNot(String nickname, Long memberId);

    @Modifying
    @Query("UPDATE MemberEntity m SET m.profileImgUrl = :profileImageUrl WHERE m.id = :memberId")
    void updateProfileImageUrl(Long memberId, String profileImageUrl);

    @Modifying
    @Query("UPDATE MemberEntity m SET m.nickname = :nickname WHERE m.id = :memberId")
    void updateNickname(Long memberId, String nickname);

    @Modifying
    @Query("UPDATE MemberEntity m SET m.locationEntity.longitude = :longitude,"
            + " m.locationEntity.latitude = :latitude , m.locationEntity.firstDistrict = :first, "
            + "m.locationEntity.secondDistrict = :second, m.locationEntity.thirdDistrict = :third WHERE m.id = :memberId")
    void updateLocation(Long memberId, Double latitude, Double longitude, String first, String second, String third);

    @Query("SELECT m FROM MemberEntity m INNER JOIN PushNotificationJpaEntity pn ON m.id = pn.memberId "
            + "WHERE pn.allowance = true")
    Slice<MemberEntity> findPushNotificationAllowingMember(Pageable pageable);

    @Query("SELECT m.locationEntity FROM MemberEntity m WHERE m.id = :id")
    Optional<LocationEntity> findLocationById(Long id);

    @Query("SELECT m.id FROM MemberEntity m where m.nickname = :nickname")
    Optional<Long> findIdByNickname(String nickname);
}
