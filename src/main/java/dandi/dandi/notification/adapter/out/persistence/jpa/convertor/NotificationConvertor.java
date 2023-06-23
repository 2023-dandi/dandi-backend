package dandi.dandi.notification.adapter.out.persistence.jpa.convertor;

import dandi.dandi.notification.adapter.out.persistence.jpa.NotificationJpaEntity;
import dandi.dandi.notification.domain.Notification;

public interface NotificationConvertor {

    boolean canConvert(NotificationJpaEntity notificationJpaEntity);

    Notification convert(NotificationJpaEntity notificationJpaEntity);
}
