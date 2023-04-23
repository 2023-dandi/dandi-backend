package dandi.dandi.auth.web.in;

import dandi.dandi.auth.application.port.in.LoginCommand;

public class LoginRequest {

    private String idToken;
    private String pushNotificationToken;

    public LoginRequest() {
    }

    public LoginRequest(String idToken, String pushNotificationToken) {
        this.idToken = idToken;
        this.pushNotificationToken = pushNotificationToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getPushNotificationToken() {
        return pushNotificationToken;
    }

    public LoginCommand toCommand() {
        return new LoginCommand(idToken, pushNotificationToken);
    }
}
