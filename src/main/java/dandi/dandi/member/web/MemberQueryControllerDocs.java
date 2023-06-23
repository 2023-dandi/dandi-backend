package dandi.dandi.member.web;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "회원체계")
public interface MemberQueryControllerDocs {

    @Operation(summary = "사용자 정보 반환", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponse(responseCode = "200", description = "사용자 정보 정상 반환")
    ResponseEntity<MemberInfoResponse> getMemberInfo(@Parameter(hidden = true) Long memberId);

    @Operation(summary = "닉네임 중복 확인", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponse(responseCode = "200", description = "닉네임 중복 확인 성공")
    ResponseEntity<NicknameDuplicationCheckResponse> checkNicknameDuplication(@Parameter(hidden = true) Long memberId,
                                                                              @RequestParam String nickname);
}
