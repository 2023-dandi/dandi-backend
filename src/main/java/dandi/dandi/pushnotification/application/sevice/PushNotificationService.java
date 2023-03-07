package dandi.dandi.pushnotification.application.sevice;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.pushnotification.application.port.in.PushNotificationAllowanceUpdateCommand;
import dandi.dandi.pushnotification.application.port.in.PushNotificationResponse;
import dandi.dandi.pushnotification.application.port.in.PushNotificationTimeUpdateCommand;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.domain.PushNotification;
import java.time.LocalTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PushNotificationService {

    private final PushNotificationPersistencePort pushNotificationPersistencePort;

    public PushNotificationService(PushNotificationPersistencePort pushNotificationPersistencePort) {
        this.pushNotificationPersistencePort = pushNotificationPersistencePort;
    }

    @Transactional(readOnly = true)
    public PushNotificationResponse findPushNotification(Long memberId) {
        PushNotification pushNotification = findPushNotificationByMemberId(memberId);
        return new PushNotificationResponse(pushNotification);
    }

    @Transactional
    public void updatePushNotificationTime(Long memberId,
                                           PushNotificationTimeUpdateCommand pushNotificationTimeUpdateCommand) {
        PushNotification pushNotification = findPushNotificationByMemberId(memberId);
        LocalTime pushNotificationTime = pushNotificationTimeUpdateCommand.getPushNotificationTime();
        pushNotificationPersistencePort.updatePushNotificationTime(pushNotification.getId(), pushNotificationTime);
    }

    @Transactional
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
