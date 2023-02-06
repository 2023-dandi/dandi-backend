package dandi.dandi.auth.application.dto;

public class LoginResponse {

    private final String token;
    private final boolean newUser;

    public LoginResponse(String token, boolean newUser) {
        this.token = token;
        this.newUser = newUser;
    }

    public static LoginResponse newUser(String token) {
        return new LoginResponse(token, true);
    }

    public static LoginResponse existingUser(String token) {
        return new LoginResponse(token, false);
    }

    public String getToken() {
        return token;
    }

    public boolean isNewUser() {
        return newUser;
    }
}
