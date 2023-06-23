package dandi.dandi.notification.application.port.in;

import org.springframework.data.domain.Pageable;

public interface NotificationQueryServicePort {

    NotificationResponses getNotifications(Long memberId, Pageable pageable);
}
