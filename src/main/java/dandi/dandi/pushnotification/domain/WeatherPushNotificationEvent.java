package dandi.dandi.pushnotification.domain;

import java.time.LocalDate;

public class WeatherPushNotificationEvent {

    private final Long memberId;
    private final LocalDate weatherDate;

    public WeatherPushNotificationEvent(Long memberId, LocalDate weatherDate) {
        this.memberId = memberId;
        this.weatherDate = weatherDate;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getWeatherDate() {
        return weatherDate;
    }
}
