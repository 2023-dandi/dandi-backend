package dandi.dandi.pushnotification.application.service;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.pushnotification.application.port.in.PushNotificationAllowanceUpdateCommand;
import dandi.dandi.pushnotification.application.port.in.PushNotificationCommandServicePort;
import dandi.dandi.pushnotification.application.port.in.PushNotificationTimeUpdateCommand;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.domain.PushNotification;
import java.time.LocalTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PushNotificationCommandServiceAdapter implements PushNotificationCommandServicePort {

    private final PushNotificationPersistencePort pushNotificationPersistencePort;

    public PushNotificationCommandServiceAdapter(PushNotificationPersistencePort pushNotificationPersistencePort) {
        this.pushNotificationPersistencePort = pushNotificationPersistencePort;
    }

    public void updatePushNotificationTime(Long memberId,
                                           PushNotificationTimeUpdateCommand pushNotificationTimeUpdateCommand) {
        PushNotification pushNotification = findPushNotificationByMemberId(memberId);
        LocalTime pushNotificationTime = pushNotificationTimeUpdateCommand.getPushNotificationTime();
        pushNotificationPersistencePort.updatePushNotificationTime(pushNotification.getId(), pushNotificationTime);
    }

    public void updatePushNotificationAllowance(Long memberId,
                                                PushNotificationAllowanceUpdateCommand pushNotificationAllowanceUpdateCommand) {
        PushNotification pushNotification = findPushNotificationByMemberId(memberId);
        boolean allowance = pushNotificationAllowanceUpdateCommand.isAllowed();
        pushNotificationPersistencePort.updatePushNotificationAllowance(pushNotification.getId(), allowance);
    }

    private PushNotification findPushNotificationByMemberId(Long memberId) {
        return pushNotificationPersistencePort.findPushNotificationByMemberId(memberId)
                .orElseThrow(() -> InternalServerException.pushNotificationNotFound(memberId));
    }
}
