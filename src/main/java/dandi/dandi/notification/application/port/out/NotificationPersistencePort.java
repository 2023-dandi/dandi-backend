package dandi.dandi.notification.application.port.out;

import dandi.dandi.notification.domain.Notification;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationPersistencePort {

    void save(Notification notification);

    Optional<Notification> findById(Long id);

    Slice<Notification> findByMemberId(Long memberId, Pageable pageable);

    void updateCheckTrue(Long id);
}
