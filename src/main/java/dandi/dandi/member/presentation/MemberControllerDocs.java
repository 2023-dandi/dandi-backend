package dandi.dandi.member.presentation;

import dandi.dandi.member.application.dto.MemberInfoResponse;
import dandi.dandi.member.application.dto.NicknameUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원체계")
public interface MemberControllerDocs {

    @Operation(summary = "Apple ID로 로그인/회원가입", parameters = @Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 정상 반환"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
    })
    ResponseEntity<MemberInfoResponse> getMemberNickname(@Parameter(hidden = true) Long memberId);

    @Operation(summary = "닉네임 변경", parameters = @Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "닉네임 정상 변경"),
            @ApiResponse(responseCode = "400", description = "규칙에 어긋나는 닉네임")
    })
    ResponseEntity<Void> updateMemberNickname(@Parameter(hidden = true) Long memberId,
                                              NicknameUpdateRequest nicknameUpdateRequest);
}
