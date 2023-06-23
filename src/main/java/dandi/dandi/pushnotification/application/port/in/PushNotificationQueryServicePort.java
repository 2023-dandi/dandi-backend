package dandi.dandi.pushnotification.application.port.in;

public interface PushNotificationQueryServicePort {

    PushNotificationResponse findPushNotification(Long memberId);
}
