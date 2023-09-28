package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.adapter.out.kma.dto.*;
import dandi.dandi.weather.adapter.out.kma.extractor.*;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.application.port.out.WeatherRequestRetryableException;
import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.WeatherLocation;
import dandi.dandi.weather.domain.Weathers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static dandi.dandi.weather.adapter.out.kma.KmaConstant.KMA_DATE_FORMATTER;
import static dandi.dandi.weather.adapter.out.kma.KmaConstant.KMA_TIME_FORMATTER;
import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.*;
import static dandi.dandi.weather.domain.PrecipitationType.RAIN;
import static dandi.dandi.weather.domain.Sky.CLOUDY;
import static dandi.dandi.weather.domain.WindDirection.NW;
import static dandi.dandi.weather.domain.WindDirection.SE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class KmaWeatherRequesterTest {

    private static final Long WEATHER_LOCATION_ID = 1L;
    private static final LocalDateTime BASE_DATE_TIME = LocalDateTime.of(2023, 8, 31, 14, 0);
    private static final String BASE_TIME = BASE_DATE_TIME.format(KMA_TIME_FORMATTER);
    private static final String BASE_DATE = BASE_DATE_TIME.format(KMA_DATE_FORMATTER);
    private static final WeatherLocation WEATHER_LOCATION = new WeatherLocation(WEATHER_LOCATION_ID, 60, 127);

    private final KmaWeatherApiCaller weatherApiCaller = mock(KmaWeatherApiCaller.class);
    private final String kmaServiceKey = "SERVICE_KEY";
    private final WeatherExtractors weatherExtractors = new WeatherExtractors(List.of(
            new DoNothingExtractor(), new PrecipitationAmountExtractor(), new PrecipitationTypeExtractor(),
            new SkyTypeExtractor(), new WindSpeedExtractor(), new HumidityExtractor(), new WindDirectionExtractor(),
            new PrecipitationPossibilityExtractor(), new TemperatureExtractor()));
    private final KmaWeatherRequester kmaWeatherRequester =
            new KmaWeatherRequester(weatherApiCaller, kmaServiceKey, weatherExtractors);

    @DisplayName("날씨 정보를 받아올 수 있다.")
    @Test
    void getWeather() {
        WeatherRequest weatherRequest = new WeatherRequest(kmaServiceKey, "JSON", BASE_DATE, BASE_TIME, 900, 60, 127);
        WeatherResponseHeader header = new WeatherResponseHeader("00", "NORMAL_SERVICE");
        WeatherResponseBody body = new WeatherResponseBody("JSON", generateWeatherItems(), 900, 0, 900);
        KmaWeatherResponses kmaWeatherResponses = new KmaWeatherResponses(new WeatherResponse(header, body));
        when(weatherApiCaller.getWeathers(weatherRequest))
                .thenReturn(kmaWeatherResponses);

        Weathers weathers = kmaWeatherRequester.getWeathers(BASE_DATE_TIME, WEATHER_LOCATION);

        assertAll(
                () -> assertThat(weathers.getWeatherLocationId()).isEqualTo(WEATHER_LOCATION_ID),
                () -> assertThat(weathers.getValues()).isEqualTo(generateExpectedWeathers())
        );
    }

    @DisplayName("날씨 요청의 응답으로 재시도 할 수 있는 응답 코드를 받는다면 RetryableException을 발생시킨다.")
    @Test
    void getWeather_RetryableException() {
        WeatherResponseHeader header = new WeatherResponseHeader("04", "HTTP_ERROR");
        WeatherResponseBody body = null;
        WeatherRequest weatherRequest = new WeatherRequest(kmaServiceKey, "JSON", BASE_DATE, BASE_TIME, 900, 60, 127);
        KmaWeatherResponses kmaWeatherResponses = new KmaWeatherResponses(new WeatherResponse(header, body));
        when(weatherApiCaller.getWeathers(weatherRequest))
                .thenReturn(kmaWeatherResponses);

        assertThatThrownBy(() -> kmaWeatherRequester.getWeathers(BASE_DATE_TIME, WEATHER_LOCATION))
                .isInstanceOf(WeatherRequestRetryableException.class)
                .hasMessage("HTTP_ERROR");
    }

    @DisplayName("날씨 요청의 응답으로 재시도 할 수 없는 응답 코드를 받는다면 FatalException을 발생시킨다.")
    @Test
    void getWeather_FatalException() {
        WeatherResponseHeader header = new WeatherResponseHeader("10", "INVALID_REQUEST_PARAMETER_ERROR");
        WeatherResponseBody body = null;
        WeatherRequest weatherRequest = new WeatherRequest(kmaServiceKey, "JSON", BASE_DATE, BASE_TIME, 900, 60, 127);
        KmaWeatherResponses kmaWeatherResponses = new KmaWeatherResponses(new WeatherResponse(header, body));
        when(weatherApiCaller.getWeathers(weatherRequest))
                .thenReturn(kmaWeatherResponses);

        assertThatThrownBy(() -> kmaWeatherRequester.getWeathers(BASE_DATE_TIME, WEATHER_LOCATION))
                .isInstanceOf(WeatherRequestFatalException.class)
                .hasMessage("INVALID_REQUEST_PARAMETER_ERROR");
    }

    @Test
    @DisplayName("날씨 요청의 응답으로 해당 위치에 날씨 데이터가 존재하지 않다는 응답 코드를 받으면 FatalException을 발생시킨다.")
    void getWeather_FatalException_NoData() {
        WeatherResponseHeader header = new WeatherResponseHeader("03", "NO_DATA_ERROR");
        WeatherResponseBody body = null;
        WeatherRequest weatherRequest = new WeatherRequest(kmaServiceKey, "JSON", BASE_DATE, BASE_TIME, 900, 60, 127);
        KmaWeatherResponses kmaWeatherResponses = new KmaWeatherResponses(new WeatherResponse(header, body));
        when(weatherApiCaller.getWeathers(weatherRequest))
                .thenReturn(kmaWeatherResponses);

        assertThatThrownBy(() -> kmaWeatherRequester.getWeathers(BASE_DATE_TIME, WEATHER_LOCATION))
                .isInstanceOf(WeatherRequestFatalException.class)
                .hasMessage("위치에 대한 날씨 정보가 존재하지 않습니다.(60, 127)");
    }

    private WeatherItems generateWeatherItems() {
        String firstFsctTime = "0500";
        String secondFsctTime = "0600";
        return new WeatherItems(List.of(
                generateWeatherItem(firstFsctTime, TMP.name(), "15"),
                generateWeatherItem(firstFsctTime, POP.name(), "60"),
                generateWeatherItem(firstFsctTime, PTY.name(), "1"),
                generateWeatherItem(firstFsctTime, PCP.name(), "강수없음"),
                generateWeatherItem(firstFsctTime, REH.name(), "70"),
                generateWeatherItem(firstFsctTime, VEC.name(), "165"),
                generateWeatherItem(firstFsctTime, WSD.name(), "2.0"),
                generateWeatherItem(firstFsctTime, SKY.name(), "3"),
                generateWeatherItem(secondFsctTime, TMP.name(), "16"),
                generateWeatherItem(secondFsctTime, POP.name(), "50"),
                generateWeatherItem(secondFsctTime, PTY.name(), "1"),
                generateWeatherItem(secondFsctTime, PCP.name(), "2.0mm"),
                generateWeatherItem(secondFsctTime, REH.name(), "80"),
                generateWeatherItem(secondFsctTime, VEC.name(), "339"),
                generateWeatherItem(secondFsctTime, WSD.name(), "1.5"),
                generateWeatherItem(secondFsctTime, SKY.name(), "3")
        ));
    }

    private WeatherItem generateWeatherItem(String fsctTime, String category, String fsctValue) {
        return new WeatherItem("20230825", "0400", category, "20230825", fsctTime, fsctValue, "60", "128");
    }

    private List<Weather> generateExpectedWeathers() {
        return List.of(
                new Weather.WeatherBuilder(LocalDateTime.of(2023, 8, 25, 5, 0))
                        .temperature(15.0)
                        .precipitationPossibility(60)
                        .precipitationType(RAIN)
                        .precipitationAmount(0.0)
                        .humidity(70)
                        .windDirection(SE)
                        .windSpeed(2.0)
                        .sky(CLOUDY)
                        .build(),
                new Weather.WeatherBuilder(LocalDateTime.of(2023, 8, 25, 6, 0))
                        .temperature(16.0)
                        .precipitationPossibility(50)
                        .precipitationType(RAIN)
                        .precipitationAmount(2.0)
                        .humidity(80)
                        .windDirection(NW)
                        .windSpeed(1.5)
                        .sky(CLOUDY)
                        .build()
        );
    }
}
