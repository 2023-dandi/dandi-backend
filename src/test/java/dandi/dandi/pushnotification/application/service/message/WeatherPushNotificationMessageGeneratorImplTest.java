package dandi.dandi.pushnotification.application.service.message;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.weather.application.port.out.WeatherForecastResult;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class WeatherPushNotificationMessageGeneratorImplTest {

    private final WeatherPushNotificationMessageGeneratorImpl weatherPushNotificationMessageGenerator =
            new WeatherPushNotificationMessageGeneratorImpl();

    @DisplayName("날씨 정보를 받아 날씨 푸시 알림 메시지를 생성한다.")
    @ParameterizedTest
    @MethodSource("provideWeatherForecastResponseAndExpectedMessage")
    void generateMessage(WeatherForecastResult weatherForecastResult, String expectedMessage) {
        String pushMessage = weatherPushNotificationMessageGenerator.generateMessage(weatherForecastResult);

        assertThat(pushMessage).isEqualTo(expectedMessage);
    }

    private static Stream<Arguments> provideWeatherForecastResponseAndExpectedMessage() {
        return Stream.of(
                Arguments.of(WeatherForecastResult.ofSuccess(10, 20),
                        "오늘 날씨는 최저 10 / 최고 20입니다. 단디에서 옷차림을 확인해보세요"),
                Arguments.of(WeatherForecastResult.ofSuccessButLocationUpdate(10, 20),
                        "오늘 날씨는 최저 10 / 최고 20입니다. 위치 정보를 수정해서 날씨 정확도를 높여주세요!"),
                Arguments.of(WeatherForecastResult.ofFailure("DB_ERROR", true),
                        "단디에서 최저 최고 기온에 따른 옷차림을 확인해보세요!")
        );
    }
}
