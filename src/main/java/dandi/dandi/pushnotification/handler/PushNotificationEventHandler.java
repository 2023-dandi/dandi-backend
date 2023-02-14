package dandi.dandi.pushnotification.handler;

import dandi.dandi.member.domain.NewMemberCreatedEvent;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Async("asyncExecutor")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PushNotificationEventHandler {

    private final PushNotificationRepository pushNotificationRepository;

    public PushNotificationEventHandler(PushNotificationRepository pushNotificationRepository) {
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @TransactionalEventListener
    public void savePushNotificationByMember(NewMemberCreatedEvent newMemberCreatedEvent) {
        PushNotification pushNotification = PushNotification.initial(newMemberCreatedEvent.getMemberId());
        pushNotificationRepository.save(pushNotification);
    }
}
