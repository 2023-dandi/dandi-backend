package dandi.dandi.notification.adapter.in.web;

import dandi.dandi.notification.application.port.in.NotificationResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "알림")
public interface NotificationControllerDocs {

    @Operation(summary = "알림 조회", parameters = {@Parameter(name = "size"), @Parameter(name = "page"),
            @Parameter(name = "sort"), @Parameter(example = "DESC")})
    @ApiResponse(responseCode = "200", description = "알림 정상 반환")
    ResponseEntity<NotificationResponses> getNotifications(@Parameter(hidden = true) Long memberId,
                                                           @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "알림 확인 여부 true로 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "알림 확인 여부 정상 변경"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 알림"),
            @ApiResponse(responseCode = "403", description = "다른 사용자의 알림 확인 여부 변경 시도")
    })
    ResponseEntity<Void> checkNotification(@Parameter(hidden = true) Long memberId, @PathVariable Long notificationId);
}
