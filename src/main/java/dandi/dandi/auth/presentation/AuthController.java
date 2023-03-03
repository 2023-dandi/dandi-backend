package dandi.dandi.auth.presentation;

import static dandi.dandi.auth.support.RefreshTokenCookieProvider.REFRESH_TOKEN;

import dandi.dandi.auth.application.AuthService;
import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.auth.application.dto.LoginResponse;
import dandi.dandi.auth.application.dto.TokenRefreshResponse;
import dandi.dandi.auth.support.Login;
import dandi.dandi.auth.support.RefreshTokenCookieProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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
        ResponseCookie refreshTokenCookie =
                refreshTokenCookieGenerator.createCookie(loginResponse.getRefreshToken());
        return bodyBuilder.header(HttpHeaders.AUTHORIZATION, loginResponse.getAccessToken())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@Login Long memberId,
                                        @CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken) {
        TokenRefreshResponse tokenRefreshResponse = authService.refresh(memberId, refreshToken);
        ResponseCookie refreshTokenCookie =
                refreshTokenCookieGenerator.createCookie(tokenRefreshResponse.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, tokenRefreshResponse.getAccessToken())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Login Long memberId) {
        authService.logout(memberId);
        return ResponseEntity.noContent().build();
    }
}
