package dandi.dandi.auth.presentation;

import static dandi.dandi.auth.support.RefreshTokenCookieProvider.REFRESH_TOKEN;
import static org.springframework.http.HttpStatus.CREATED;

import dandi.dandi.auth.application.AuthService;
import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.auth.application.dto.LoginResponse;
import dandi.dandi.auth.application.dto.TokenResponse;
import dandi.dandi.auth.support.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/oauth/apple")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.getToken(loginRequest);
        if (loginResponse.isNewUser()) {
            return ResponseEntity.status(CREATED).body(new TokenResponse(loginResponse));
        }
        return ResponseEntity.ok(new TokenResponse(loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Login Long memberId,
                                                 @CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken) {
        TokenResponse tokenResponse = authService.refresh(memberId, refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Login Long memberId) {
        authService.logout(memberId);
        return ResponseEntity.noContent().build();
    }
}
