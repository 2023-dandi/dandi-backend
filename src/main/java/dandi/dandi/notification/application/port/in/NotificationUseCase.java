package dandi.dandi.notification.application.port.in;

import org.springframework.data.domain.Pageable;

public interface NotificationUseCase {

    NotificationResponses getNotifications(Long memberId, Pageable pageable);

    void checkNotification(Long memberId, Long notificationId);
}
