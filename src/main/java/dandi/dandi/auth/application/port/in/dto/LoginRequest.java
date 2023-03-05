package dandi.dandi.auth.application.port.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginRequest {

    @Schema(example = "Identity Token From Apple Server")
    private String idToken;

    public LoginRequest() {
    }

    public LoginRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }
}
