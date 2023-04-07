package dandi.dandi.notification.application;

import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Async("asyncExecutor")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class NotificationEventHandler {

    private final Logger logger = LoggerFactory.getLogger(NotificationEventHandler.class);

    protected final NotificationPersistencePort notificationPersistencePort;

    public NotificationEventHandler(NotificationPersistencePort notificationPersistencePort) {
        this.notificationPersistencePort = notificationPersistencePort;
    }

    protected void saveNotification(Notification notification) {
        try {
            notificationPersistencePort.save(notification);
        } catch (Exception e) {
            logger.info("알림 저장 실패 : {}", notification);
        }
    }
}
