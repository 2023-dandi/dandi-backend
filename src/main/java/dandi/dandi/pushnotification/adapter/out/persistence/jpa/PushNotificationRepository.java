package dandi.dandi.pushnotification.adapter.out.persistence.jpa;

import java.time.LocalTime;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PushNotificationRepository extends JpaRepository<PushNotificationJpaEntity, Long> {

    Optional<PushNotificationJpaEntity> findPushNotificationByMemberId(Long memberId);

    @Modifying
    @Query("UPDATE PushNotificationJpaEntity pn SET pn.pushNotificationTime = :pushNotificationTime WHERE pn.id = :id")
    void updatePushNotificationTime(Long id, LocalTime pushNotificationTime);

    @Modifying
    @Query("UPDATE PushNotificationJpaEntity pn SET pn.allowance = :allowance WHERE pn.id = :id")
    void updatePushNotificationAllowance(Long id, boolean allowance);

    @Modifying
    @Query("UPDATE PushNotificationJpaEntity pn SET pn.token = :pushNotificationToken WHERE pn.id = :id")
    void updatePushNotificationToken(Long id, String pushNotificationToken);

    @Query("SELECT pn FROM PushNotificationJpaEntity pn WHERE pn.allowance = true")
    Slice<PushNotificationJpaEntity> findAllowedPushNotification(Pageable pageable);
}
