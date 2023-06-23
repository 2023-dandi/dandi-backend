package dandi.dandi.notification.adapter.in.web;

import dandi.dandi.notification.application.port.in.NotificationResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "알림")
public interface NotificationQueryControllerDocs {

    @Operation(summary = "알림 조회", parameters = {@Parameter(name = "size"), @Parameter(name = "page"),
            @Parameter(name = "sort"), @Parameter(example = "DESC")})
    @ApiResponse(responseCode = "200", description = "알림 정상 반환")
    ResponseEntity<NotificationResponses> getNotifications(@Parameter(hidden = true) Long memberId,
                                                           @Parameter(hidden = true) Pageable pageable);
}
