package dandi.dandi.pushnotification.presentation;

import dandi.dandi.auth.support.Login;
import dandi.dandi.pushnotification.application.PushNotificationService;
import dandi.dandi.pushnotification.application.dto.PushNotificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushNotificationController implements PushNotificationControllerDocs {

    private final PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @GetMapping("/push-notification")
    public ResponseEntity<PushNotificationResponse> getPushNotification(@Login Long memberId) {
        return ResponseEntity.ok(pushNotificationService.findPushNotification(memberId));
    }
}
