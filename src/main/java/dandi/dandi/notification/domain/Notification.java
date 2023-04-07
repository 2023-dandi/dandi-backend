package dandi.dandi.notification.domain;

import java.time.LocalDate;

public abstract class Notification {

    private final Long id;
    private final Long memberId;
    private final NotificationType type;

    protected Notification(Long id, Long memberId, NotificationType type) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
    }

    public final Long getId() {
        return id;
    }

    public final Long getMemberId() {
        return memberId;
    }

    public final NotificationType getType() {
        return type;
    }

    public abstract Long getPostId();

    public abstract Long getCommentId();

    public abstract String getCommentContent();

    public abstract LocalDate getWeatherDate();
}
