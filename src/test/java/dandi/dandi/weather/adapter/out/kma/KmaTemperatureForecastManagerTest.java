package dandi.dandi.weather.adapter.out.kma;

import static dandi.dandi.member.MemberTestFixture.DISTRICT;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.member.domain.District;
import dandi.dandi.member.domain.Location;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherItems;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherRequest;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponse;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponseBody;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponseHeader;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponses;
import dandi.dandi.weather.application.port.out.WeatherForecastResult;
import dandi.dandi.weather.application.port.out.WeatherForecastResultCode;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KmaTemperatureForecastManagerTest {

    private static final LocalDate NOW = LocalDate.of(2023, 5, 17);
    private static final Location LOCATION = new Location(35.82795555555556, 128.53049722222224, DISTRICT);
    private static final WeatherRequest WEATHER_REQUEST = new WeatherRequest("serviceKey", "JSON", "20230517", "0200",
            300, 88, 90);
    private static final int MIN_TEMPERATURE = 10;
    private static final int MAX_TEMPERATURE = 21;

    private static final List<WeatherItem> weatherItems = List.of(
            new WeatherItem("20230517", "0200", "TMX", "20230517", "1400", "21", "60", "127"),
            new WeatherItem("20230517", "0200", "TMN", "20230517", "0500", "10", "60", "127"));
    private static final WeatherResponses SUCCESS_WEATHER_RESPONSES = new WeatherResponses(new WeatherResponse(
            new WeatherResponseHeader("00", "NORMAL_SERVICE"),
            new WeatherResponseBody("JSON", new WeatherItems(weatherItems), 100, 1, 100))
    );
    private static final WeatherResponses UNKNOWN_ERROR_WEATHER_RESPONSES = new WeatherResponses(new WeatherResponse(
            new WeatherResponseHeader("99", "UNKNOWN_ERROR"), new WeatherResponseBody()));
    private static final WeatherResponses NETWORK_ERROR_WEATHER_RESPONSES = new WeatherResponses(new WeatherResponse(
            new WeatherResponseHeader("05", "SERVICE_TIME_OUT"), new WeatherResponseBody()));
    private static final WeatherResponses NO_DATA_ERROR_WEATHER_RESPONSES = new WeatherResponses(new WeatherResponse(
            new WeatherResponseHeader("03", "NODATA_ERROR"), new WeatherResponseBody()));

    private final KmaWeatherApiCaller weatherApiCaller = Mockito.mock(KmaWeatherApiCaller.class);
    private final String serviceKey = "serviceKey";
    private final KmaCoordinateConvertor kmaCoordinateConvertor = new KmaCoordinateConvertor();
    private final TemperatureForecastExtractor temperatureForecastExtractor = new TemperatureForecastExtractor();
    private final KmaTemperatureForecastManager kmaTemperatureForecastManager = new KmaTemperatureForecastManager(
            weatherApiCaller, serviceKey, kmaCoordinateConvertor, temperatureForecastExtractor);

    @DisplayName("기상청에서 날씨 정보를 정상적으로 받아올 수 있다.")
    @Test
    void getForecasts_Success() {
        when(weatherApiCaller.getWeathers(WEATHER_REQUEST))
                .thenReturn(SUCCESS_WEATHER_RESPONSES);

        WeatherForecastResult result = kmaTemperatureForecastManager.getForecasts(NOW, LOCATION);

        assertAll(
                () -> assertThat(result.getResultCode()).isEqualTo(WeatherForecastResultCode.SUCCESS),
                () -> assertThat(result.getMinTemperature()).isEqualTo(MIN_TEMPERATURE),
                () -> assertThat(result.getMaxTemperature()).isEqualTo(MAX_TEMPERATURE)
        );
    }

    @DisplayName("캐시에 저장된 기상청 날씨 정보를 받아올 수 있다.")
    @Test
    void getForecasts_Success_CacheHit() {
        when(weatherApiCaller.getWeathers(WEATHER_REQUEST))
                .thenReturn(SUCCESS_WEATHER_RESPONSES);
        kmaTemperatureForecastManager.getForecasts(NOW, LOCATION);

        WeatherForecastResult result = kmaTemperatureForecastManager.getForecasts(NOW, LOCATION);

        assertAll(
                () -> assertThat(result.getResultCode()).isEqualTo(WeatherForecastResultCode.SUCCESS),
                () -> assertThat(result.getMinTemperature()).isEqualTo(MIN_TEMPERATURE),
                () -> assertThat(result.getMaxTemperature()).isEqualTo(MAX_TEMPERATURE),
                () -> verify(weatherApiCaller, only()).getWeathers(any())
        );
    }

    @DisplayName("날씨 API 요청에서 재시도 할 수 없는 에러를 응답받는다면 재시도 가능 여부와 FAILURE을 응답한다.")
    @Test
    void getForecasts_Failure() {
        when(weatherApiCaller.getWeathers(WEATHER_REQUEST))
                .thenReturn(UNKNOWN_ERROR_WEATHER_RESPONSES);

        WeatherForecastResult result = kmaTemperatureForecastManager.getForecasts(NOW, LOCATION);

        assertAll(
                () -> assertThat(result.getResultCode()).isEqualTo(WeatherForecastResultCode.FAILURE),
                () -> assertThat(result.getFailureMessage()).isEqualTo("UNKNOWN_ERROR"),
                () -> assertThat(result.isNonRetryableFailure()).isTrue()
        );
    }

    @DisplayName("날씨 API 요청에서 재시도 할 수 있는 에러를 응답받는다면 재시도 가능 여부와 FAILURE을 응답한다")
    @Test
    void getForecasts_RetrySuccessAfterNetworkError() {
        when(weatherApiCaller.getWeathers(WEATHER_REQUEST))
                .thenReturn(NETWORK_ERROR_WEATHER_RESPONSES);

        WeatherForecastResult result = kmaTemperatureForecastManager.getForecasts(NOW, LOCATION);

        assertAll(
                () -> assertThat(result.getResultCode()).isEqualTo(WeatherForecastResultCode.FAILURE),
                () -> assertThat(result.getFailureMessage()).isEqualTo("SERVICE_TIME_OUT"),
                () -> assertThat(result.isRetryableFailure()).isTrue()
        );
    }

    @DisplayName("날씨 API 요청에서 위치 정보 오류에 의한 NODATA_ERROR 응답을 받은 후 기본 위치 정보 기반 날씨 API 요청 재시도에 성공하면 SUCCESS_BUT_LOCATION_UPDATE를 응답한다.")
    @Test
    void getForecasts_RetrySuccessAfterNoDataError() {
        when(weatherApiCaller.getWeathers(WEATHER_REQUEST))
                .thenReturn(NO_DATA_ERROR_WEATHER_RESPONSES);
        WeatherRequest locationErrorHandleWeatherRequest =
                new WeatherRequest("serviceKey", "JSON", "20230517", "0200", 300, 67, 100);
        when(weatherApiCaller.getWeathers(locationErrorHandleWeatherRequest))
                .thenReturn(SUCCESS_WEATHER_RESPONSES);

        WeatherForecastResult result = kmaTemperatureForecastManager.getForecasts(NOW, LOCATION);

        assertAll(
                () -> assertThat(result.getResultCode())
                        .isEqualTo(WeatherForecastResultCode.SUCCESS_BUT_LOCATION_UPDATE),
                () -> assertThat(result.getMinTemperature()).isEqualTo(MIN_TEMPERATURE),
                () -> assertThat(result.getMaxTemperature()).isEqualTo(MAX_TEMPERATURE)
        );
    }

    @DisplayName("날씨 API 요청에서 위치 정보 오류에 의한 NODATA_ERROR 응답을 받은 후 기본 위치 정보 기반 재시도에 캐시에 있는 날씨 정보 조회에 성공하면 SUCCESS_BUT_LOCATION_UPDATE를 응답한다.")
    @Test
    void getForecasts_RetrySuccessAfterNoDataError_RetryCacheHit() {
        District defaultDistrict = new District("대전광역시");
        Location defaultLocation = new Location(36.347119444444445, 127.38656666666667, defaultDistrict);
        WeatherRequest defaultLocationWeatherRequest = new WeatherRequest(
                "serviceKey", "JSON", "20230517", "0200", 300, 67, 100);
        when(weatherApiCaller.getWeathers(defaultLocationWeatherRequest))
                .thenReturn(SUCCESS_WEATHER_RESPONSES);
        kmaTemperatureForecastManager.getForecasts(NOW, defaultLocation);
        when(weatherApiCaller.getWeathers(WEATHER_REQUEST))
                .thenReturn(NO_DATA_ERROR_WEATHER_RESPONSES);

        WeatherForecastResult result = kmaTemperatureForecastManager.getForecasts(NOW, LOCATION);

        assertAll(
                () -> assertThat(result.getResultCode())
                        .isEqualTo(WeatherForecastResultCode.SUCCESS_BUT_LOCATION_UPDATE),
                () -> assertThat(result.getMinTemperature()).isEqualTo(MIN_TEMPERATURE),
                () -> assertThat(result.getMaxTemperature()).isEqualTo(MAX_TEMPERATURE),
                () -> verify(weatherApiCaller, times(2)).getWeathers(any())
        );
    }

    @DisplayName("날씨 API 요청에서 위치 정보 오류에 의한 NODATA_ERROR 응답을 받은 후 기본 위치 정보 기반 캐시 조회와 API 요청 재시도에 모두 실패하면 FAILURE을 응답한다.")
    @Test
    void getForecasts_RetryFailureAfterNoDataError() {
        when(weatherApiCaller.getWeathers(WEATHER_REQUEST))
                .thenReturn(NO_DATA_ERROR_WEATHER_RESPONSES);
        WeatherRequest locationErrorHandleWeatherRequest =
                new WeatherRequest("serviceKey", "JSON", "20230517", "0200", 300, 67, 100);
        when(weatherApiCaller.getWeathers(locationErrorHandleWeatherRequest))
                .thenReturn(UNKNOWN_ERROR_WEATHER_RESPONSES);

        WeatherForecastResult result = kmaTemperatureForecastManager.getForecasts(NOW, LOCATION);

        assertAll(
                () -> assertThat(result.getResultCode()).isEqualTo(WeatherForecastResultCode.FAILURE),
                () -> assertThat(result.getFailureMessage()).isEqualTo("UNKNOWN_ERROR")
        );
    }
}
