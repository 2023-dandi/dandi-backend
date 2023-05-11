package dandi.dandi.pushnotification.application.sevice.message;

import dandi.dandi.pushnotification.application.port.out.weather.WeatherForecast;
import org.springframework.stereotype.Component;

@Component
public class WeatherPushNotificationMessageGeneratorImpl implements WeatherPushNotificationMessageGenerator {

    private static final String MESSAGE_FORMAT = "오늘 날씨는 최저 %d / 최고 %d입니다. 단디에서 옷차림을 확인해보세요";

    @Override
    public String generateMessage(WeatherForecast weatherForecast) {
        return String.format(MESSAGE_FORMAT, weatherForecast.getMinTemperature(), weatherForecast.getMaxTemperature());
    }
}
