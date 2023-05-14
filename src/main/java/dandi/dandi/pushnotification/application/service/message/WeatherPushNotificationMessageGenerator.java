package dandi.dandi.pushnotification.application.service.message;

import dandi.dandi.weather.application.port.out.WeatherForecast;

public interface WeatherPushNotificationMessageGenerator {

    String generateMessage(WeatherForecast weatherForecast);
}
