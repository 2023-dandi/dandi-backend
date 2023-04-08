package dandi.dandi.notification.application.port.out;

import dandi.dandi.notification.domain.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationPersistencePort {

    void save(Notification notification);

    Slice<Notification> findByMemberId(Long memberId, Pageable pageable);
}
