package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.member.domain.Location;
import dandi.dandi.weather.adapter.out.kma.dto.TemperatureDto;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherRequest;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponse;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponseBody;
import dandi.dandi.weather.application.port.out.WeatherForecastInfoManager;
import dandi.dandi.weather.application.port.out.WeatherForecastResponse;
import dandi.dandi.weather.application.port.out.WeatherForecastResultCode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KmaTemperatureForecastManager implements WeatherForecastInfoManager {

    private static final DateTimeFormatter KMA_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String DATA_TYPE = "JSON";
    private static final String BASE_TIME = "0200";
    private static final int DEFAULT_NX = 60;
    private static final int DEFAULT_NY = 127;
    private static final int ROW_COUNT = 300;

    private final KmaWeatherApiCaller weatherApiCaller;
    private final String kmaServiceKey;
    private final KmaCoordinateConvertor kmaCoordinateConvertor;
    private final TemperatureForecastExtractor temperatureForecastExtractor;
    private final WeatherRequest locationErrorHandleWeatherRequest;

    public KmaTemperatureForecastManager(KmaWeatherApiCaller weatherApiCaller,
                                         @Value("${weather.kma.service-key}") String kmaServiceKey,
                                         KmaCoordinateConvertor kmaCoordinateConvertor,
                                         TemperatureForecastExtractor temperatureForecastExtractor) {
        this.weatherApiCaller = weatherApiCaller;
        this.kmaServiceKey = kmaServiceKey;
        this.kmaCoordinateConvertor = kmaCoordinateConvertor;
        this.temperatureForecastExtractor = temperatureForecastExtractor;
        this.locationErrorHandleWeatherRequest =
                new WeatherRequest(kmaServiceKey, DATA_TYPE, BASE_TIME, ROW_COUNT, DEFAULT_NX, DEFAULT_NY);
    }

    public WeatherForecastResponse getForecasts(LocalDate now, Location location) {
        String baseDate = now.format(KMA_DATE_FORMATTER);
        Coordinate coordinate = kmaCoordinateConvertor.convert(location.getLatitude(), location.getLongitude());
        WeatherRequest kmaWeatherRequest = new WeatherRequest(
                kmaServiceKey, DATA_TYPE, baseDate, BASE_TIME, ROW_COUNT, coordinate.getNx(), coordinate.getNy());
        return requestWeatherForecast(kmaWeatherRequest);
    }

    private WeatherForecastResponse requestWeatherForecast(WeatherRequest weatherRequest) {
        WeatherResponse weatherResponse = weatherApiCaller.getWeathers(weatherRequest)
                .getResponse();
        KmaResponseCode responseCode = extractResultCode(weatherResponse);
        if (responseCode.isSuccessful()) {
            return generateSuccessfulWeatherForecastResponse(
                    WeatherForecastResultCode.SUCCESS, weatherRequest.getBase_date(), weatherResponse.getBody());
        }
        return retry(responseCode, weatherRequest);
    }

    private WeatherForecastResponse generateSuccessfulWeatherForecastResponse(WeatherForecastResultCode resultCode,
                                                                              String baseDate,
                                                                              WeatherResponseBody body) {
        List<WeatherItem> temperatureForecasts = body.getItems()
                .getItem()
                .stream()
                .filter(kmaWeatherItem -> kmaWeatherItem.getFcstDate().equals(baseDate))
                .collect(Collectors.toUnmodifiableList());
        TemperatureDto temperature = temperatureForecastExtractor.extract(temperatureForecasts);
        return WeatherForecastResponse.ofSuccess(
                resultCode, temperature.getMinTemperature(), temperature.getMaxTemperature());
    }

    private WeatherForecastResponse retry(KmaResponseCode responseCode, WeatherRequest weatherRequest) {
        if (responseCode.isRetryableNetworkError()) {
            return retry(WeatherForecastResultCode.SUCCESS, weatherRequest);
        }
        if (responseCode.isErrorAssociatedWithLocation()) {
            WeatherRequest defaultRetryRequest =
                    locationErrorHandleWeatherRequest.ofBaseDate(weatherRequest.getBase_date());
            return retry(WeatherForecastResultCode.SUCCESS_BUT_LOCATION_UPDATE, defaultRetryRequest);
        }
        return WeatherForecastResponse.ofFailure(responseCode.name());
    }

    private WeatherForecastResponse retry(WeatherForecastResultCode successResultCode, WeatherRequest weatherRequest) {
        WeatherResponse weatherResponse = weatherApiCaller.getWeathers(weatherRequest)
                .getResponse();
        KmaResponseCode responseCode = extractResultCode(weatherResponse);
        if (!responseCode.isSuccessful()) {
            return WeatherForecastResponse.ofFailure(responseCode.name());
        }
        return generateSuccessfulWeatherForecastResponse(
                successResultCode, weatherRequest.getBase_date(), weatherResponse.getBody());
    }

    private KmaResponseCode extractResultCode(WeatherResponse kmaWeatherResponse) {
        String resultCode = kmaWeatherResponse.getHeader()
                .getResultCode();
        return KmaResponseCode.from(resultCode);
    }
}
