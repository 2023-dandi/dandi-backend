package dandi.dandi.pushnotification.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.pushnotification.application.port.in.PushNotificationResponse;
import dandi.dandi.pushnotification.application.service.PushNotificationService;
import dandi.dandi.pushnotification.web.dto.PushNotificationAllowanceUpdateRequest;
import dandi.dandi.pushnotification.web.dto.PushNotificationTimeUpdateRequest;
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
        pushNotificationService.updatePushNotificationTime(memberId, pushNotificationTimeUpdateRequest.toCommand());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/allowance")
    public ResponseEntity<Void> updatePushNotificationAllowance(@Login Long memberId,
                                                                @RequestBody PushNotificationAllowanceUpdateRequest pushNotificationAllowanceUpdateRequest) {
        pushNotificationService.updatePushNotificationAllowance(memberId,
                pushNotificationAllowanceUpdateRequest.toCommand());
        return ResponseEntity.noContent().build();
    }
}
