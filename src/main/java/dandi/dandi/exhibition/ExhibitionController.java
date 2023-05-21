package dandi.dandi.exhibition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//전시회 푸시 알림을 위한 임시 객체
@Tag(name = "전시회용")
@RestController
@RequestMapping("/exhibition")
public class ExhibitionController {

    private final PushNotificationSender pushNotificationSender;

    public ExhibitionController(PushNotificationSender pushNotificationSender) {
        this.pushNotificationSender = pushNotificationSender;
    }

    @Operation(summary = "현재 날씨 푸시 알림 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "푸시 알림 발송 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 닉네임"),
    })
    @PostMapping("/weather")
    public ResponseEntity<Void> pushWeatherNotification(@RequestBody ExhibitionPushNotificationRequest request) {
        pushNotificationSender.pushWeatherNotification(request.getNickname());
        return ResponseEntity.noContent().build();
    }
}
