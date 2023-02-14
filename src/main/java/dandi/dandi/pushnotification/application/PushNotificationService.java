package dandi.dandi.pushnotification.application;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.pushnotification.application.dto.PushNotificationResponse;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PushNotificationService {

    private final PushNotificationRepository pushNotificationRepository;

    public PushNotificationService(PushNotificationRepository pushNotificationRepository) {
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @Transactional(readOnly = true)
    public PushNotificationResponse findPushNotification(Long memberId) {
        PushNotification pushNotification = pushNotificationRepository.findPushNotificationByMemberId(memberId)
                .orElseThrow(() -> InternalServerException.pushNotificationNotFound(memberId));
        return new PushNotificationResponse(pushNotification);
    }
}
