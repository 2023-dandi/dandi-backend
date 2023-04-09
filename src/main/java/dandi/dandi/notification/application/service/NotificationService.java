package dandi.dandi.notification.application.service;

import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.notification.application.port.in.NotificationResponse;
import dandi.dandi.notification.application.port.in.NotificationResponses;
import dandi.dandi.notification.application.port.in.NotificationUseCase;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService implements NotificationUseCase {

    private final NotificationPersistencePort notificationPersistencePort;

    public NotificationService(NotificationPersistencePort notificationPersistencePort) {
        this.notificationPersistencePort = notificationPersistencePort;
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponses getNotifications(Long memberId, Pageable pageable) {
        Slice<Notification> notifications = notificationPersistencePort.findByMemberId(memberId, pageable);
        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toUnmodifiableList());
        return new NotificationResponses(notificationResponses, notifications.isLast());
    }

    @Override
    @Transactional
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
