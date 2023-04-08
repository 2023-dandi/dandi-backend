package dandi.dandi.notification.adapter.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationJpaEntity, Long> {

    Slice<NotificationJpaEntity> findByMemberId(Long memberId, Pageable pageable);
}
