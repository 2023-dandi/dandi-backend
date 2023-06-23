package dandi.dandi.pushnotification.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.pushnotification.adapter.in.web.dto.PushNotificationAllowanceUpdateRequest;
import dandi.dandi.pushnotification.adapter.in.web.dto.PushNotificationTimeUpdateRequest;
import dandi.dandi.pushnotification.application.port.in.PushNotificationCommandServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/push-notification")
public class PushNotificationCommandController implements PushNotificationCommandControllerDocs {

    private final PushNotificationCommandServicePort pushNotificationCommandServicePort;

    public PushNotificationCommandController(PushNotificationCommandServicePort pushNotificationCommandServicePort) {
        this.pushNotificationCommandServicePort = pushNotificationCommandServicePort;
    }

    @PatchMapping("/time")
    public ResponseEntity<Void> updatePushNotificationTime(@Login Long memberId,
                                                           @RequestBody PushNotificationTimeUpdateRequest pushNotificationTimeUpdateRequest) {
        pushNotificationCommandServicePort.updatePushNotificationTime(memberId,
                pushNotificationTimeUpdateRequest.toCommand());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/allowance")
    public ResponseEntity<Void> updatePushNotificationAllowance(@Login Long memberId,
                                                                @RequestBody PushNotificationAllowanceUpdateRequest pushNotificationAllowanceUpdateRequest) {
        pushNotificationCommandServicePort.updatePushNotificationAllowance(
                memberId, pushNotificationAllowanceUpdateRequest.toCommand());
        return ResponseEntity.noContent().build();
    }
}
