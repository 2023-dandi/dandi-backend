package dandi.dandi.notification.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.notification.application.port.in.NotificationResponses;
import dandi.dandi.notification.application.port.in.NotificationUseCase;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final NotificationUseCase notificationUseCase;

    public NotificationController(NotificationUseCase notificationUseCase) {
        this.notificationUseCase = notificationUseCase;
    }

    @GetMapping("/notifications")
    public ResponseEntity<NotificationResponses> getNotifications(@Login Long memberId, Pageable pageable) {
        return ResponseEntity.ok(notificationUseCase.getNotifications(memberId, pageable));
    }
}
