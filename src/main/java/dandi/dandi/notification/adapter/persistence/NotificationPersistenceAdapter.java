package dandi.dandi.notification.adapter.persistence;

import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationPersistenceAdapter implements NotificationPersistencePort {

    private final NotificationRepository notificationRepository;

    public NotificationPersistenceAdapter(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void save(Notification notification) {
        NotificationJpaEntity notificationJpaEntity = NotificationJpaEntity.fromNotification(notification);
        notificationRepository.save(notificationJpaEntity);
    }
}
