package dandi.dandi.auth.presentation;

import dandi.dandi.auth.application.AuthService;
import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.auth.application.dto.LoginResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/oauth/apple")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.getAccessToken(loginRequest);
        if (loginResponse.isNewUser()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.AUTHORIZATION, loginResponse.getToken())
                    .build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, loginResponse.getToken())
                .build();
    }
}
