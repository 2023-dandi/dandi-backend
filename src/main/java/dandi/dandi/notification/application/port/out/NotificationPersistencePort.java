package dandi.dandi.notification.application.port.out;

import dandi.dandi.notification.domain.Notification;

public interface NotificationPersistencePort {

    void save(Notification notification);
}
