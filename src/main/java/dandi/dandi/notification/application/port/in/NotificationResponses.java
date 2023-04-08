package dandi.dandi.notification.application.port.in;

import java.util.List;

public class NotificationResponses {

    private List<NotificationResponse> notifications;
    private boolean lastPage;

    public NotificationResponses() {
    }

    public NotificationResponses(List<NotificationResponse> notifications, boolean lastPage) {
        this.notifications = notifications;
        this.lastPage = lastPage;
    }

    public List<NotificationResponse> getNotifications() {
        return notifications;
    }

    public boolean isLastPage() {
        return lastPage;
    }
}
