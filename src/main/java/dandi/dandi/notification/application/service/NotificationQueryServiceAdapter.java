package dandi.dandi.notification.application.service;

import dandi.dandi.notification.application.port.in.NotificationQueryServicePort;
import dandi.dandi.notification.application.port.in.NotificationResponse;
import dandi.dandi.notification.application.port.in.NotificationResponses;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NotificationQueryServiceAdapter implements NotificationQueryServicePort {

    private final NotificationPersistencePort notificationPersistencePort;

    public NotificationQueryServiceAdapter(NotificationPersistencePort notificationPersistencePort) {
        this.notificationPersistencePort = notificationPersistencePort;
    }

    @Override
    public NotificationResponses getNotifications(Long memberId, Pageable pageable) {
        Slice<Notification> notifications = notificationPersistencePort.findByMemberId(memberId, pageable);
        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toUnmodifiableList());
        return new NotificationResponses(notificationResponses, notifications.isLast());
    }
}
