package dandi.dandi.pushnotification.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

    Optional<PushNotification> findPushNotificationByMemberId(Long memberId);
}
