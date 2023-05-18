package dandi.dandi.pushnotification.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationSource)) {
            return false;
        }
        PushNotificationSource that = (PushNotificationSource) o;
        return Objects.equals(token, that.token) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, body);
    }
}
