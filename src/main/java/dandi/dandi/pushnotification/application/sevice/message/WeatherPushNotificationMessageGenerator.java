package dandi.dandi.pushnotification.application.sevice.message;

import dandi.dandi.pushnotification.application.port.out.weather.WeatherForecast;

public interface WeatherPushNotificationMessageGenerator {

    String generateMessage(WeatherForecast weatherForecast);
}
