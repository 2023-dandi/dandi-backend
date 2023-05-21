package dandi.dandi.exhibition;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.pushnotification.application.service.PushNotificationSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//전시회 푸시 알림을 위한 임시 객체
@RestController
@RequestMapping("/exhibition")
public class ExhibitionController {

    private final PushNotificationSender pushNotificationSender;

    public ExhibitionController(PushNotificationSender pushNotificationSender) {
        this.pushNotificationSender = pushNotificationSender;
    }

    @PostMapping("/weather")
    public ResponseEntity<Void> pushWeatherNotification(@Login Long memberId) {
        pushNotificationSender.pushWeatherNotification(memberId);
        return ResponseEntity.noContent().build();
    }
}
