package dandi.dandi.pushnotification.application;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.pushnotification.application.dto.PushNotificationAllowanceUpdateRequest;
import dandi.dandi.pushnotification.application.dto.PushNotificationResponse;
import dandi.dandi.pushnotification.application.dto.PushNotificationTimeUpdateRequest;
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
        PushNotification pushNotification = findPushNotificationByMemberId(memberId);
        return new PushNotificationResponse(pushNotification);
    }

    @Transactional
    public void updatePushNotificationTime(Long memberId,
                                           PushNotificationTimeUpdateRequest pushNotificationTimeUpdateRequest) {
        PushNotification pushNotification = findPushNotificationByMemberId(memberId);
        pushNotification.updatePushNotificationTime(pushNotificationTimeUpdateRequest.getNewPushNotificationTime());
    }

    @Transactional
    public void updatePushNotificationAllowance(Long memberId,
                                                PushNotificationAllowanceUpdateRequest pushNotificationAllowanceUpdateRequest) {
        PushNotification pushNotification = findPushNotificationByMemberId(memberId);
        pushNotification.updateAllowance(pushNotificationAllowanceUpdateRequest.isAllowed());
    }

    private PushNotification findPushNotificationByMemberId(Long memberId) {
        return pushNotificationRepository.findPushNotificationByMemberId(memberId)
                .orElseThrow(() -> InternalServerException.pushNotificationNotFound(memberId));
    }
}
