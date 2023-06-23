package dandi.dandi.pushnotification.application.service;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.pushnotification.application.port.in.PushNotificationQueryServicePort;
import dandi.dandi.pushnotification.application.port.in.PushNotificationResponse;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.domain.PushNotification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PushNotificationQueryServiceAdapter implements PushNotificationQueryServicePort {

    private final PushNotificationPersistencePort pushNotificationPersistencePort;

    public PushNotificationQueryServiceAdapter(PushNotificationPersistencePort pushNotificationPersistencePort) {
        this.pushNotificationPersistencePort = pushNotificationPersistencePort;
    }

    public PushNotificationResponse findPushNotification(Long memberId) {
        PushNotification pushNotification = pushNotificationPersistencePort.findPushNotificationByMemberId(memberId)
                .orElseThrow(() -> InternalServerException.pushNotificationNotFound(memberId));
        return new PushNotificationResponse(pushNotification);
    }
}
