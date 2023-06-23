package dandi.dandi.notification.adapter.out.persistence.jpa;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<NotificationJpaEntity, Long> {

    Slice<NotificationJpaEntity> findByMemberId(Long memberId, Pageable pageable);

    @Modifying
    @Query("UPDATE NotificationJpaEntity n SET n.checked = TRUE WHERE n.id = :id")
    void updateCheckTrue(Long id);
}
