package dandi.dandi.auth.application.port.in;

import io.swagger.v3.oas.annotations.media.Schema;

public class TokenResponse {

    @Schema(example = "Access Token")
    private String accessToken;

    @Schema(example = "Refresh Token")
    private String refreshToken;

    public TokenResponse(LoginResponse loginResponse) {
        this.accessToken = loginResponse.getAccessToken();
        this.refreshToken = loginResponse.getRefreshToken();
    }

    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
