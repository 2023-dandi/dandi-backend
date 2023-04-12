package dandi.dandi.notification.adapter.persistence.convertor;

import dandi.dandi.notification.adapter.persistence.NotificationJpaEntity;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.NotificationType;
import dandi.dandi.notification.domain.WeatherNotification;
import org.springframework.stereotype.Component;

@Component
public class WeatherNotificationConvertor implements NotificationConvertor {

    @Override
    public boolean canConvert(NotificationJpaEntity notificationJpaEntity) {
        return notificationJpaEntity.hasNotificationType(NotificationType.WEATHER);
    }

    @Override
    public Notification convert(NotificationJpaEntity notificationJpaEntity) {
        return new WeatherNotification(
                notificationJpaEntity.getId(),
                notificationJpaEntity.getMemberId(),
                notificationJpaEntity.getNotificationType(),
                notificationJpaEntity.getCreatedAt().toLocalDate(),
                notificationJpaEntity.isChecked(),
                notificationJpaEntity.getWeatherDate()
        );
    }
}
