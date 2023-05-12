package dandi.dandi.pushnotification.application.port.out.webpush;

public interface WebPushManager {

    void pushMessage(String token, String title, String body);
}
