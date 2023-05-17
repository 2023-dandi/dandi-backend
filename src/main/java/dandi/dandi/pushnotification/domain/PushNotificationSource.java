package dandi.dandi.pushnotification.domain;

public class PushNotificationSource {

    private final String token;
    private final String body;

    public PushNotificationSource(String token, String body) {
        this.token = token;
        this.body = body;
    }

    public String getToken() {
        return token;
    }

    public String getBody() {
        return body;
    }
}
