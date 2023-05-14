package dandi.dandi.pushnotification.application.port.out.persistence;

import dandi.dandi.pushnotification.domain.PushNotification;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PushNotificationPersistencePort {

    Optional<PushNotification> findPushNotificationByMemberId(Long memberId);

    PushNotification save(PushNotification pushNotification);

    void updatePushNotificationTime(Long id, LocalTime pushNotificationTime);

    void updatePushNotificationAllowance(Long id, boolean allowance);

    void updatePushNotificationToken(Long id, String pushNotificationToken);

    Slice<PushNotification> findAllowedPushNotification(Pageable pageable);
}
