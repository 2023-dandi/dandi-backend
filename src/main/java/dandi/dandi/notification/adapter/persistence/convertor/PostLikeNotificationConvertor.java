package dandi.dandi.notification.adapter.persistence.convertor;

import dandi.dandi.notification.adapter.persistence.NotificationJpaEntity;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.NotificationType;
import dandi.dandi.notification.domain.PostLikeNotification;
import org.springframework.stereotype.Component;

@Component
public class PostLikeNotificationConvertor implements NotificationConvertor {

    @Override
    public boolean canConvert(NotificationJpaEntity notificationJpaEntity) {
        return notificationJpaEntity.hasNotificationType(NotificationType.POST_LIKE);
    }

    @Override
    public Notification convert(NotificationJpaEntity notificationJpaEntity) {
        return new PostLikeNotification(
                notificationJpaEntity.getId(),
                notificationJpaEntity.getMemberId(),
                notificationJpaEntity.getNotificationType(),
                notificationJpaEntity.getCreatedAt().toLocalDate(),
                notificationJpaEntity.isChecked(),
                notificationJpaEntity.getPostId()
        );
    }
}
