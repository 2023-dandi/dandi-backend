package dandi.dandi.notification.adapter.out.persistence.jpa;

import dandi.dandi.notification.adapter.out.persistence.jpa.convertor.NotificationConvertors;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.Notification;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
public class NotificationPersistenceAdapter implements NotificationPersistencePort {

    private final NotificationRepository notificationRepository;
    private final NotificationConvertors notificationConvertors;

    public NotificationPersistenceAdapter(NotificationRepository notificationRepository,
                                          NotificationConvertors notificationConvertors) {
        this.notificationRepository = notificationRepository;
        this.notificationConvertors = notificationConvertors;
    }

    @Override
    public void save(Notification notification) {
        NotificationJpaEntity notificationJpaEntity = NotificationJpaEntity.fromNotification(notification);
        notificationRepository.save(notificationJpaEntity);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id)
                .map(notificationConvertors::convert);
    }

    @Override
    public void updateCheckTrue(Long id) {
        notificationRepository.updateCheckTrue(id);
    }

    @Override
    public Slice<Notification> findByMemberId(Long memberId, Pageable pageable) {
        Slice<NotificationJpaEntity> notificationJpaEntities =
                notificationRepository.findByMemberId(memberId, pageable);
        List<Notification> notifications = notificationJpaEntities.stream()
                .map(notificationConvertors::convert)
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(notifications, pageable, notificationJpaEntities.hasNext());
    }
}
