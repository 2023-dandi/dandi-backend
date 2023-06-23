package dandi.dandi.notification.application.port.in;

public interface NotificationCommandServicePort {

    void checkNotification(Long memberId, Long notificationId);
}
