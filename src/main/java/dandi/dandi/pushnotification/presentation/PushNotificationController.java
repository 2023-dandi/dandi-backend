package dandi.dandi.pushnotification.presentation;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.pushnotification.application.dto.PushNotificationAllowanceUpdateRequest;
import dandi.dandi.pushnotification.application.dto.PushNotificationResponse;
import dandi.dandi.pushnotification.application.dto.PushNotificationTimeUpdateRequest;
import dandi.dandi.pushnotification.application.sevice.PushNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/push-notification")
public class PushNotificationController implements PushNotificationControllerDocs {

    private final PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @GetMapping
    public ResponseEntity<PushNotificationResponse> getPushNotification(@Login Long memberId) {
        return ResponseEntity.ok(pushNotificationService.findPushNotification(memberId));
    }

    @PatchMapping("/time")
    public ResponseEntity<Void> updatePushNotificationTime(@Login Long memberId,
                                                           @RequestBody PushNotificationTimeUpdateRequest pushNotificationTimeUpdateRequest) {
        pushNotificationService.updatePushNotificationTime(memberId, pushNotificationTimeUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/allowance")
    public ResponseEntity<Void> updatePushNotificationAllowance(@Login Long memberId,
                                                                @RequestBody PushNotificationAllowanceUpdateRequest pushNotificationAllowanceUpdateRequest) {
        pushNotificationService.updatePushNotificationAllowance(memberId, pushNotificationAllowanceUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
