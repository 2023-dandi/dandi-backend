package dandi.dandi.notification.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.notification.application.port.in.NotificationCommandServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController implements NotificationControllerDocs {

    private final NotificationCommandServicePort notificationCommandServicePort;

    public NotificationController(NotificationCommandServicePort notificationCommandServicePort) {
        this.notificationCommandServicePort = notificationCommandServicePort;
    }

    @PutMapping("/notifications/{notificationId}/check")
    public ResponseEntity<Void> checkNotification(@Login Long memberId, @PathVariable Long notificationId) {
        notificationCommandServicePort.checkNotification(memberId, notificationId);
        return ResponseEntity.noContent().build();
    }
}
