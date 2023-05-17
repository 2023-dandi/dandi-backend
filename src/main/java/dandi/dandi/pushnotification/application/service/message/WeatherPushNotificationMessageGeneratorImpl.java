package dandi.dandi.pushnotification.application.service.message;

import dandi.dandi.weather.application.port.out.WeatherForecastResult;
import org.springframework.stereotype.Component;

@Component
public class WeatherPushNotificationMessageGeneratorImpl implements WeatherPushNotificationMessageGenerator {

    private static final String FAILED_MESSAGE = "단디에서 최저 최고 기온에 따른 옷차림을 확인해보세요!";
    private static final String WEATHER_MESSAGE = "오늘 날씨는 최저 %d / 최고 %d입니다. ";
    private static final String SUCCESS_MESSAGE_FORMAT = WEATHER_MESSAGE + "단디에서 옷차림을 확인해보세요";
    private static final String SUCCESS_BUT_LOCATION_UPDATE_MESSAGE_FORMAT =
            WEATHER_MESSAGE + "위치 정보를 수정해서 날씨 정확도를 높여주세요!";

    @Override
    public String generateMessage(WeatherForecastResult weatherForecastResult) {
        if (weatherForecastResult.isFailed()) {
            return FAILED_MESSAGE;
        } else if (weatherForecastResult.isSuccess()) {
            return String.format(SUCCESS_MESSAGE_FORMAT,
                    weatherForecastResult.getMinTemperature(), weatherForecastResult.getMaxTemperature());
        }
        return String.format(SUCCESS_BUT_LOCATION_UPDATE_MESSAGE_FORMAT,
                weatherForecastResult.getMinTemperature(), weatherForecastResult.getMaxTemperature());
    }
}
