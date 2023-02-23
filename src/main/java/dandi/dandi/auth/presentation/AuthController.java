package dandi.dandi.auth.presentation;

import dandi.dandi.auth.application.AuthService;
import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.auth.application.dto.LoginResponse;
import dandi.dandi.auth.support.RefreshTokenCookieProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;
    private final RefreshTokenCookieProvider refreshTokenCookieGenerator;

    public AuthController(AuthService authService, RefreshTokenCookieProvider refreshTokenCookieGenerator) {
        this.authService = authService;
        this.refreshTokenCookieGenerator = refreshTokenCookieGenerator;
    }

    @PostMapping("/login/oauth/apple")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.getToken(loginRequest);
        if (loginResponse.isNewUser()) {
            return generateResponseEntityWithToken(ResponseEntity.status(HttpStatus.CREATED), loginResponse);
        }
        return generateResponseEntityWithToken(ResponseEntity.ok(), loginResponse);
    }

    private ResponseEntity<Void> generateResponseEntityWithToken(BodyBuilder bodyBuilder, LoginResponse loginResponse) {
        ResponseCookie refreshTokenCookieProviderCookie =
                refreshTokenCookieGenerator.createCookie(loginResponse.getRefreshToken());
        return bodyBuilder.header(HttpHeaders.AUTHORIZATION, loginResponse.getAccessToken())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookieProviderCookie.toString())
                .build();
    }
}
