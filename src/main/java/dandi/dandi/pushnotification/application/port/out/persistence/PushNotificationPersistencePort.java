package dandi.dandi.pushnotification.application.port.out.persistence;

import dandi.dandi.pushnotification.domain.PushNotification;
import java.time.LocalTime;
import java.util.Optional;

public interface PushNotificationPersistencePort {

    Optional<PushNotification> findPushNotificationByMemberId(Long memberId);

    PushNotification save(PushNotification pushNotification);

    void updatePushNotificationTime(Long id, LocalTime pushNotificationTime);

    void updatePushNotificationAllowance(Long id, boolean allowance);
}
