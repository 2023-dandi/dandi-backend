package dandi.dandi.pushnotification.application.port.in;

public interface PushNotificationCommandServicePort {

    void updatePushNotificationTime(Long memberId,
                                    PushNotificationTimeUpdateCommand pushNotificationTimeUpdateCommand);

    void updatePushNotificationAllowance(Long memberId,
                                         PushNotificationAllowanceUpdateCommand pushNotificationAllowanceUpdateCommand);
}
