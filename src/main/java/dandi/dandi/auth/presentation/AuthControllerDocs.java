package dandi.dandi.auth.presentation;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.auth.application.dto.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
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
            @ApiResponse(responseCode = "200", description = "로그인",
                    headers = {
                            @Header(name = AUTHORIZATION, description = "Access Token"),
                            @Header(name = SET_COOKIE, description = "Refresh Token")}),
            @ApiResponse(responseCode = "201", description = "회원가입 후 로그인",
                    headers = {
                            @Header(name = AUTHORIZATION, description = "Access Token"),
                            @Header(name = SET_COOKIE, description = "Refresh Token")}),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 Apple Id Token",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> login(@Parameter(description = "사용자 id") LoginRequest loginRequest);

    @Operation(summary = "Token Refresh")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh 성공",
                    headers = {
                            @Header(name = AUTHORIZATION, description = "Access Token"),
                            @Header(name = SET_COOKIE, description = "Refresh Token")}),
            @ApiResponse(responseCode = "401", description = "만료된 Refresh Token \t\n" +
                    "존재하지 않거나 조작된 Refresh Token",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    }
    )
    ResponseEntity<Void> refresh(@Parameter(hidden = true) Long memberId,
                                 @Parameter(hidden = true) String refreshToken);
}
