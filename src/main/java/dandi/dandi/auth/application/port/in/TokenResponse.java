package dandi.dandi.auth.application.port.in;

public class TokenResponse {

    private String accessToken;
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
