package dandi.dandi.auth.web;

import static dandi.dandi.auth.web.support.RefreshTokenCookieProvider.REFRESH_TOKEN;
import static org.springframework.http.HttpStatus.CREATED;

import dandi.dandi.auth.application.port.in.AuthUseCase;
import dandi.dandi.auth.application.port.in.LoginResponse;
import dandi.dandi.auth.application.port.in.TokenResponse;
import dandi.dandi.auth.web.in.LoginRequest;
import dandi.dandi.auth.web.support.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/login/oauth/apple")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authUseCase.getToken(loginRequest.toCommand());
        if (loginResponse.isNewUser()) {
            return ResponseEntity.status(CREATED).body(new TokenResponse(loginResponse));
        }
        return ResponseEntity.ok(new TokenResponse(loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Login Long memberId,
                                                 @CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken) {
        TokenResponse tokenResponse = authUseCase.refresh(memberId, refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Login Long memberId) {
        authUseCase.logout(memberId);
        return ResponseEntity.noContent().build();
    }
}
