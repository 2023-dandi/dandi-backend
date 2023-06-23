package dandi.dandi.notification.application.service;

import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.notification.application.port.in.NotificationCommandServicePort;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationCommandServiceAdapter implements NotificationCommandServicePort {

    private final NotificationPersistencePort notificationPersistencePort;

    public NotificationCommandServiceAdapter(NotificationPersistencePort notificationPersistencePort) {
        this.notificationPersistencePort = notificationPersistencePort;
    }

    @Override
    public void checkNotification(Long memberId, Long notificationId) {
        Notification notification = notificationPersistencePort.findById(notificationId)
                .orElseThrow(NotFoundException::notification);
        validateModification(notification, memberId);
        notificationPersistencePort.updateCheckTrue(notificationId);
    }

    private void validateModification(Notification notification, Long memberId) {
        if (!notification.isOwnedBy(memberId)) {
            throw ForbiddenException.notificationCheckModification();
        }
    }
}
