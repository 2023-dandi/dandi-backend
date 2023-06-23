package dandi.dandi.notification.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.notification.application.port.in.NotificationResponses;
import dandi.dandi.notification.application.port.in.NotificationUseCase;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController implements NotificationControllerDocs {

    private final NotificationUseCase notificationUseCase;

    public NotificationController(NotificationUseCase notificationUseCase) {
        this.notificationUseCase = notificationUseCase;
    }

    @GetMapping("/notifications")
    public ResponseEntity<NotificationResponses> getNotifications(@Login Long memberId, Pageable pageable) {
        return ResponseEntity.ok(notificationUseCase.getNotifications(memberId, pageable));
    }

    @PutMapping("/notifications/{notificationId}/check")
    public ResponseEntity<Void> checkNotification(@Login Long memberId, @PathVariable Long notificationId) {
        notificationUseCase.checkNotification(memberId, notificationId);
        return ResponseEntity.noContent().build();
    }
}
