package dandi.dandi.exhibition;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<Void> pushWeatherNotification(@RequestBody ExhibitionPushNotificationRequest request) {
        pushNotificationSender.pushWeatherNotification(request.getNickname());
        return ResponseEntity.noContent().build();
    }
}
