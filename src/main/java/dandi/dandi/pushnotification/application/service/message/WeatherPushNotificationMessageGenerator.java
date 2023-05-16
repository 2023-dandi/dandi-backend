package dandi.dandi.pushnotification.application.service.message;

import dandi.dandi.weather.application.port.out.WeatherForecastResponse;

public interface WeatherPushNotificationMessageGenerator {

    String generateMessage(WeatherForecastResponse weatherForecastResponse);
}
