package dandi.dandi.pushnotification.application.sevice.message;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.pushnotification.application.port.out.weather.WeatherForecast;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WeatherPushNotificationMessageGeneratorImplTest {

    private final WeatherPushNotificationMessageGeneratorImpl weatherPushNotificationMessageGenerator =
            new WeatherPushNotificationMessageGeneratorImpl();

    @DisplayName("날씨 예보를 받아 날씨 푸시 알림 메시지를 생성한다.")
    @Test
    void generateMessage() {
        WeatherForecast weatherForecast = new WeatherForecast(10, 20);

        String pushMessage = weatherPushNotificationMessageGenerator.generateMessage(weatherForecast);

        assertThat(pushMessage).isEqualTo("오늘 날씨는 최저 10 / 최고 20입니다. 단디에서 옷차림을 확인해보세요");
    }
}