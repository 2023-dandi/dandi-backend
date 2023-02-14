package dandi.dandi.pushnotification.presentation;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.pushnotification.application.dto.PushNotificationAllowanceUpdateRequest;
import dandi.dandi.pushnotification.application.dto.PushNotificationResponse;
import dandi.dandi.pushnotification.application.dto.PushNotificationTimeUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "푸시 알림")
public interface PushNotificationControllerDocs {

    @Operation(summary = "푸시 알림 정보 반환", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true))
    @ApiResponse(responseCode = "200", description = "푸시 알림 정보 정상 반환")
    ResponseEntity<PushNotificationResponse> getPushNotification(@Parameter(hidden = true) Long memberId);

    @Operation(summary = "푸시 알림 시간 변경", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "푸시 알림 시간 정상 변경"),
            @ApiResponse(responseCode = "400", description = "10분 단위가 아닌 푸시 알림 시간")
    })
    ResponseEntity<Void> updatePushNotificationTime(@Parameter(hidden = true) Long memberId,
                                                    PushNotificationTimeUpdateRequest pushNotificationTimeUpdateRequest);

    @Operation(summary = "푸시 알림 허용 여부 변경", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true))
    @ApiResponse(responseCode = "204", description = "푸시 알림 허용 여부 정상 변경")
    ResponseEntity<Void> updatePushNotificationAllowance(@Parameter(hidden = true) Long memberId,
                                                         PushNotificationAllowanceUpdateRequest pushNotificationAllowanceUpdateRequest);
}
