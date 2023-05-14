package dandi.dandi.pushnotification.application.handler;

import dandi.dandi.member.domain.NewMemberCreatedEvent;
import dandi.dandi.pushnotification.application.port.out.persistence.PushNotificationPersistencePort;
import dandi.dandi.pushnotification.domain.PushNotification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Async("asyncExecutor")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PushNotificationEventHandler {

    private final PushNotificationPersistencePort pushNotificationPersistencePort;

    public PushNotificationEventHandler(PushNotificationPersistencePort pushNotificationPersistencePort) {
        this.pushNotificationPersistencePort = pushNotificationPersistencePort;
    }

    @TransactionalEventListener
    public void savePushNotificationByMember(NewMemberCreatedEvent newMemberCreatedEvent) {
        PushNotification pushNotification = PushNotification.initial(
                newMemberCreatedEvent.getMemberId(),
                newMemberCreatedEvent.getPushNotificationToken());
        pushNotificationPersistencePort.save(pushNotification);
    }
}
