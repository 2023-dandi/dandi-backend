package dandi.dandi.pushnotification.adapter.out.persistence;

import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.domain.PushNotification;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationPersistenceAdapter implements PushNotificationPersistencePort {

    private final PushNotificationRepository pushNotificationRepository;

    public PushNotificationPersistenceAdapter(PushNotificationRepository pushNotificationRepository) {
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @Override
    public PushNotification save(PushNotification pushNotification) {
        return pushNotificationRepository.save(PushNotificationJpaEntity.fromPushNotification(pushNotification))
                .toPushNotification();
    }

    @Override
    public Optional<PushNotification> findPushNotificationByMemberId(Long memberId) {
        return pushNotificationRepository.findPushNotificationByMemberId(memberId)
                .map(PushNotificationJpaEntity::toPushNotification);
    }

    @Override
    public void updatePushNotificationTime(Long id, LocalTime pushNotificationTime) {
        pushNotificationRepository.updatePushNotificationTime(id, pushNotificationTime);
    }

    @Override
    public void updatePushNotificationAllowance(Long id, boolean allowance) {
        pushNotificationRepository.updatePushNotificationAllowance(id, allowance);
    }
}
