package dandi.dandi.auth.web;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.COOKIE;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.auth.application.port.in.TokenResponse;
import dandi.dandi.auth.web.in.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증")
public interface AuthControllerDocs {

    @Operation(summary = "Apple ID로 로그인/회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인"),
            @ApiResponse(responseCode = "201", description = "회원가입 후 로그인"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 Apple Id Token",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<TokenResponse> login(@Parameter(description = "사용자 id") LoginRequest loginRequest);

    @Operation(summary = "Token Refresh", parameters = {
            @Parameter(name = COOKIE, in = ParameterIn.COOKIE, required = true, example = "Refresh-Token={$refreshToken}")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh 성공"),
            @ApiResponse(responseCode = "401", description = "만료된 Refresh Token \t\n" +
                    "존재하지 않거나 조작된 Refresh Token",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<TokenResponse> refresh(@Parameter(hidden = true) String refreshToken);

    @Operation(summary = "로그아웃", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    ResponseEntity<Void> logout(@Parameter(hidden = true) Long memberId);
}
