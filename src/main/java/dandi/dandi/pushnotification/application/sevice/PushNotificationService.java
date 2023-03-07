package dandi.dandi.pushnotification.application.sevice;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.pushnotification.application.dto.PushNotificationAllowanceUpdateRequest;
import dandi.dandi.pushnotification.application.dto.PushNotificationResponse;
import dandi.dandi.pushnotification.application.dto.PushNotificationTimeUpdateRequest;
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
                                           PushNotificationTimeUpdateRequest pushNotificationTimeUpdateRequest) {
        PushNotification pushNotification = findPushNotificationByMemberId(memberId);
        LocalTime pushNotificationTime = pushNotificationTimeUpdateRequest.getNewPushNotificationTime();
        pushNotificationPersistencePort.updatePushNotificationTime(pushNotification.getId(), pushNotificationTime);
    }

    @Transactional
    public void updatePushNotificationAllowance(Long memberId,
                                                PushNotificationAllowanceUpdateRequest pushNotificationAllowanceUpdateRequest) {
        PushNotification pushNotification = findPushNotificationByMemberId(memberId);
        boolean allowance = pushNotificationAllowanceUpdateRequest.isAllowed();
        pushNotificationPersistencePort.updatePushNotificationAllowance(pushNotification.getId(), allowance);
    }

    private PushNotification findPushNotificationByMemberId(Long memberId) {
        return pushNotificationPersistencePort.findPushNotificationByMemberId(memberId)
                .orElseThrow(() -> InternalServerException.pushNotificationNotFound(memberId));
    }
}
