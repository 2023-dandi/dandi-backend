package dandi.dandi.pushnotification.domain;

import dandi.dandi.member.domain.Location;

public class RetryableWeatherPushNotification {

    private final Long memberId;
    private final Location location;
    private final String token;

    public RetryableWeatherPushNotification(Long memberId, Location location, String token) {
        this.memberId = memberId;
        this.location = location;
        this.token = token;
    }

    public static RetryableWeatherPushNotification of(PushNotification pushNotification, Location location) {
        return new RetryableWeatherPushNotification(
                pushNotification.getMemberId(), location, pushNotification.getToken());
    }

    public Long getMemberId() {
        return memberId;
    }

    public Location getLocation() {
        return location;
    }

    public String getToken() {
        return token;
    }
}
