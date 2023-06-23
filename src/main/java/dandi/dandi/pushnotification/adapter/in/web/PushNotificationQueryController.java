package dandi.dandi.pushnotification.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.pushnotification.application.port.in.PushNotificationQueryServicePort;
import dandi.dandi.pushnotification.application.port.in.PushNotificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushNotificationQueryController implements PushNotificationQueryControllerDocs {

    private final PushNotificationQueryServicePort pushNotificationQueryServicePort;

    public PushNotificationQueryController(PushNotificationQueryServicePort pushNotificationQueryServicePort) {
        this.pushNotificationQueryServicePort = pushNotificationQueryServicePort;
    }

    @GetMapping("/push-notification")
    public ResponseEntity<PushNotificationResponse> getPushNotification(@Login Long memberId) {
        return ResponseEntity.ok(pushNotificationQueryServicePort.findPushNotification(memberId));
    }
}
