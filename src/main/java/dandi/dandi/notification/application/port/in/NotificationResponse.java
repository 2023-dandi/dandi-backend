package dandi.dandi.notification.application.port.in;

import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.NotificationType;
import java.time.LocalDate;

public class NotificationResponse {

    private Long id;
    private NotificationType type;
    private LocalDate createdAt;
    private boolean checked;
    private Long postId;
    private Long commentId;
    private String commentContent;
    private LocalDate weatherDate;

    public NotificationResponse() {
    }

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.type = notification.getType();
        this.createdAt = notification.getCreatedAt();
        this.checked = notification.isChecked();
        this.postId = notification.getPostId();
        this.commentId = notification.getCommentId();
        this.commentContent = notification.getCommentContent();
        this.weatherDate = notification.getWeatherDate();
    }

    public Long getId() {
        return id;
    }

    public NotificationType getType() {
        return type;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public boolean isChecked() {
        return checked;
    }

    public Long getCommentId() {
        return commentId;
    }

    public Long getPostId() {
        return postId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public LocalDate getWeatherDate() {
        return weatherDate;
    }
}
