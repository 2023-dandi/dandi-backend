package dandi.dandi.notification.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.notification.application.port.in.NotificationQueryServicePort;
import dandi.dandi.notification.application.port.in.NotificationResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationQueryController implements NotificationQueryControllerDocs {

    private final NotificationQueryServicePort notificationQueryServicePort;

    public NotificationQueryController(NotificationQueryServicePort notificationQueryServicePort) {
        this.notificationQueryServicePort = notificationQueryServicePort;
    }

    @GetMapping("/notifications")
    public ResponseEntity<NotificationResponses> getNotifications(@Login Long memberId, Pageable pageable) {
        return ResponseEntity.ok(notificationQueryServicePort.getNotifications(memberId, pageable));
    }
}
