package dandi.dandi.notification.adapter.persistence;

import static dandi.dandi.notification.domain.NotificationType.COMMENT;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.comment.adapter.persistence.CommentJpaEntity;
import dandi.dandi.comment.adapter.persistence.CommentRepository;
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
    private final CommentRepository commentRepository;

    public NotificationPersistenceAdapter(NotificationRepository notificationRepository,
                                          CommentRepository commentRepository) {
        this.notificationRepository = notificationRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void save(Notification notification) {
        NotificationJpaEntity notificationJpaEntity = NotificationJpaEntity.fromNotification(notification);
        notificationRepository.save(notificationJpaEntity);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id)
                .map(this::mapToNotification);
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
                .map(this::mapToNotification)
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(notifications, pageable, notificationJpaEntities.hasNext());
    }

    private Notification mapToNotification(NotificationJpaEntity notificationJpaEntity) {
        if (notificationJpaEntity.hasNotificationType(COMMENT)) {
            Long commentId = notificationJpaEntity.getCommentId();
            CommentJpaEntity commentJpaEntity = commentRepository.findById(commentId)
                    .orElseThrow(() -> InternalServerException.notificationCommentNotFound(commentId));
            return notificationJpaEntity.toPostCommentNotification(commentJpaEntity.getContent());
        }
        return notificationJpaEntity.toNotification();
    }
}
