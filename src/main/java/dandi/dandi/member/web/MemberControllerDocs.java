package dandi.dandi.member.web;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.web.dto.in.LocationUpdateRequest;
import dandi.dandi.member.web.dto.in.NicknameUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "회원체계")
public interface MemberControllerDocs {

    @Operation(summary = "사용자 정보 반환", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponse(responseCode = "200", description = "사용자 정보 정상 반환")
    ResponseEntity<MemberInfoResponse> getMemberInfo(@Parameter(hidden = true) Long memberId);

    @Operation(summary = "닉네임 중복 확인", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponse(responseCode = "200", description = "닉네임 중복 확인 성공")
    ResponseEntity<NicknameDuplicationCheckResponse> checkNicknameDuplication(@Parameter(hidden = true) Long memberId,
                                                                              @RequestParam String nickname);

    @Operation(summary = "닉네임 변경", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "닉네임 정상 변경"),
            @ApiResponse(responseCode = "400", description = "규칙에 어긋나는 닉네임\t\n"
                    + "이미 존재하는 닉네임",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> updateMemberNickname(@Parameter(hidden = true) Long memberId,
                                              NicknameUpdateRequest nicknameUpdateRequest);

    @Operation(summary = "위치 변경", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "위치 정상 변경"),
            @ApiResponse(responseCode = "400", description = "범위에 어긋나는 경도, 위도",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> updateMemberLocation(@Parameter(hidden = true) Long memberId,
                                              LocationUpdateRequest locationUpdateRequest);
}
