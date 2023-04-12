package dandi.dandi.notification.adapter.persistence.convertor;

import dandi.dandi.notification.adapter.persistence.NotificationJpaEntity;
import dandi.dandi.notification.domain.Notification;

public interface NotificationConvertor {

    boolean canConvert(NotificationJpaEntity notificationJpaEntity);

    Notification convert(NotificationJpaEntity notificationJpaEntity);
}
