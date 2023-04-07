package dandi.dandi.notification.domain;

import java.time.LocalDate;

public abstract class PostNotification extends Notification {

    private final Long postId;

    protected PostNotification(Long id, Long memberId, NotificationType type, Long postId) {
        super(id, memberId, type);
        this.postId = postId;
    }

    @Override
    public final Long getPostId() {
        return postId;
    }

    @Override
    public final LocalDate getWeatherDate() {
        return null;
    }
}
