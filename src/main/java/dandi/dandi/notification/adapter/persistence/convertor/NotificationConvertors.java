package dandi.dandi.notification.adapter.persistence.convertor;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.notification.adapter.persistence.NotificationJpaEntity;
import dandi.dandi.notification.domain.Notification;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NotificationConvertors {

    private final List<NotificationConvertor> convertors;

    public NotificationConvertors(PostLikeNotificationConvertor postLikeNotificationConvertor,
                                  PostCommentNotificationConvertor postCommentNotificationConvertor,
                                  WeatherNotificationConvertor weatherNotificationConvertor) {
        this.convertors =
                List.of(postLikeNotificationConvertor, postCommentNotificationConvertor, weatherNotificationConvertor);
    }

    public Notification convert(NotificationJpaEntity notificationJpaEntity) {
        NotificationConvertor notificationConvertor = convertors.stream()
                .filter(convertor -> convertor.canConvert(notificationJpaEntity))
                .findAny()
                .orElseThrow(() ->
                        InternalServerException.notificationConvert(notificationJpaEntity.getNotificationType()));
        return notificationConvertor.convert(notificationJpaEntity);
    }
}
