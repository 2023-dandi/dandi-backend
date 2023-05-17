package dandi.dandi.pushnotification.application.service.message;

import dandi.dandi.weather.application.port.out.WeatherForecastResult;

public interface WeatherPushNotificationMessageGenerator {

    String generateMessage(WeatherForecastResult weatherForecastResult);
}
