package dandi.dandi.pushnotification.application.service.message;

import dandi.dandi.pushnotification.exception.WeatherMessageGenerationFailedException;
import dandi.dandi.weather.application.port.out.WeatherForecastResponse;
import org.springframework.stereotype.Component;

@Component
public class WeatherPushNotificationMessageGeneratorImpl implements WeatherPushNotificationMessageGenerator {

    private static final String WEATHER_MESSAGE = "오늘 날씨는 최저 %d / 최고 %d입니다. ";
    private static final String SUCCESS_MESSAGE_FORMAT = WEATHER_MESSAGE + "단디에서 옷차림을 확인해보세요";
    private static final String SUCCESS_BUT_LOCATION_UPDATE_MESSAGE_FORMAT =
            WEATHER_MESSAGE + "위치 정보를 수정해서 날씨 정확도를 높여주세요!";

    @Override
    public String generateMessage(WeatherForecastResponse weatherForecastResponse) {
        if (weatherForecastResponse.isFailed()) {
            throw new WeatherMessageGenerationFailedException();
        } else if (weatherForecastResponse.isSuccess()) {
            return String.format(SUCCESS_MESSAGE_FORMAT,
                    weatherForecastResponse.getMinTemperature(), weatherForecastResponse.getMaxTemperature());
        }
        return String.format(SUCCESS_BUT_LOCATION_UPDATE_MESSAGE_FORMAT,
                weatherForecastResponse.getMinTemperature(), weatherForecastResponse.getMaxTemperature());
    }
}
