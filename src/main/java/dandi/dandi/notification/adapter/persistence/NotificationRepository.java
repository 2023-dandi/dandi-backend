package dandi.dandi.notification.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationJpaEntity, Long> {
}
