package dandi.dandi.member.adapter.in.web;

import dandi.dandi.member.application.port.in.MemberBlockCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원체계")
public interface MemberBlockControllerDocs {

    @Operation(summary = "사용자 차단")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "사용자 차단 성공"),
            @ApiResponse(responseCode = "400", description = "이미 차단한 사용자"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 사용자"),
    })
    ResponseEntity<Void> blockMember(@Parameter(hidden = true) Long memberId, MemberBlockCommand memberBlockCommand);
}
