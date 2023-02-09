package dandi.dandi.auth.presentation;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.auth.application.dto.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증")
public interface AuthControllerDocs {

    @Operation(summary = "Apple ID로 로그인/회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인", headers = @Header(name = AUTHORIZATION, description = "Access Token")),
            @ApiResponse(responseCode = "201", description = "회원가입 후 로그인", headers = @Header(name = AUTHORIZATION, description = "Access Token")),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 Apple Id Token")
    })
    ResponseEntity<Void> login(@Parameter(description = "사용자 id") LoginRequest loginRequest);
}
