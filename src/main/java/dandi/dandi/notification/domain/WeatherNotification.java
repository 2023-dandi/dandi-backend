package dandi.dandi.notification.domain;

import static dandi.dandi.notification.domain.NotificationType.WEATHER;

import java.time.LocalDate;

public class WeatherNotification extends Notification {

    private final LocalDate weatherDate;

    public WeatherNotification(Long id, Long memberId, NotificationType type, LocalDate createdAt,
                               boolean checked, LocalDate weatherDate) {
        super(id, memberId, type, createdAt, checked);
        this.weatherDate = weatherDate;
    }

    public static WeatherNotification initial(Long memberId, LocalDate weatherDate) {
        return new WeatherNotification(null, memberId, WEATHER, null, false, weatherDate);
    }

    @Override
    public Long getPostId() {
        return null;
    }

    @Override
    public Long getCommentId() {
        return null;
    }

    @Override
    public String getCommentContent() {
        return null;
    }

    @Override
    public LocalDate getWeatherDate() {
        return weatherDate;
    }
}
