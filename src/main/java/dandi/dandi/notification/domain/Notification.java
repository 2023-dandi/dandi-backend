package dandi.dandi.notification.domain;

import java.time.LocalDate;

public abstract class Notification {

    private final Long id;
    private final Long memberId;
    private final NotificationType type;
    private final LocalDate createdAt;
    private final boolean checked;

    protected Notification(Long id, Long memberId, NotificationType type, LocalDate createdAt, boolean checked) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.createdAt = createdAt;
        this.checked = checked;
    }

    public final boolean isOwnedBy(Long memberId) {
        return this.memberId.equals(memberId);
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public boolean isChecked() {
        return checked;
    }
}
