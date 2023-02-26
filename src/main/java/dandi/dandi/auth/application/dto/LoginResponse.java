package dandi.dandi.auth.application.dto;

public class LoginResponse {

    private final String accessToken;
    private final String refreshToken;
    private final boolean newUser;

    public LoginResponse(String accessToken, String refreshToken, boolean newUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.newUser = newUser;
    }

    public static LoginResponse newUser(String accessToken, String refreshToken) {
        return new LoginResponse(accessToken, refreshToken, true);
    }

    public static LoginResponse existingUser(String accessToken, String refreshToken) {
        return new LoginResponse(accessToken, refreshToken, false);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isNewUser() {
        return newUser;
    }
}
