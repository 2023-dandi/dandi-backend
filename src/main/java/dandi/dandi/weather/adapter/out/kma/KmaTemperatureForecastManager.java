package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.member.domain.District;
import dandi.dandi.member.domain.Location;
import dandi.dandi.weather.adapter.out.kma.dto.Forecast;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherRequest;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponse;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponseBody;
import dandi.dandi.weather.application.port.out.WeatherForecastInfoManager;
import dandi.dandi.weather.application.port.out.WeatherForecastResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KmaTemperatureForecastManager implements WeatherForecastInfoManager {

    private static final DateTimeFormatter KMA_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String DATA_TYPE = "JSON";
    private static final String BASE_TIME = "0200";
    private static final int DEFAULT_NX = 67;
    private static final int DEFAULT_NY = 100;
    private static final District DEFAULT_DISTRICT = new District("대전광역시");
    private static final int ROW_COUNT = 300;

    private final KmaWeatherApiCaller weatherApiCaller;
    private final String kmaServiceKey;
    private final KmaCoordinateConvertor kmaCoordinateConvertor;
    private final TemperatureForecastExtractor temperatureForecastExtractor;
    private final WeatherRequest locationErrorHandleWeatherRequest;
    private final Map<District, Forecast> forecastCache = new ConcurrentHashMap<>();

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

    public WeatherForecastResult getForecasts(LocalDate now, Location location) {
        if (forecastCache.containsKey(location.getDistrict())) {
            Forecast forecast = forecastCache.get(location.getDistrict());
            return WeatherForecastResult.ofSuccess(forecast.getMinTemperature(), forecast.getMaxTemperature());
        }
        return requestWeatherForecast(now, location);
    }

    private WeatherForecastResult requestWeatherForecast(LocalDate now, Location location) {
        WeatherRequest weatherRequest = generateWeatherRequest(now, location);
        WeatherResponse weatherResponse = weatherApiCaller.getWeathers(weatherRequest).getResponse();
        KmaResponseCode responseCode = extractResultCode(weatherResponse);
        if (responseCode.isSuccessful()) {
            Forecast forecast = extractTemperatures(weatherRequest.getBase_date(), weatherResponse.getBody());
            forecastCache.put(location.getDistrict(), forecast);
            return WeatherForecastResult.ofSuccess(forecast.getMinTemperature(), forecast.getMaxTemperature());
        } else if (responseCode.isErrorAssociatedWithLocation()) {
            return retryWithDefaultLocation(weatherRequest.getBase_date());
        }
        return WeatherForecastResult.ofFailure(responseCode.name(), responseCode.isRetryable());
    }

    @NotNull
    private WeatherRequest generateWeatherRequest(LocalDate now, Location location) {
        Coordinate coordinate = kmaCoordinateConvertor.convert(location.getLatitude(), location.getLongitude());
        String baseDate = now.format(KMA_DATE_FORMATTER);
        return new WeatherRequest(
                kmaServiceKey, DATA_TYPE, baseDate, BASE_TIME, ROW_COUNT, coordinate.getNx(), coordinate.getNy());
    }

    private WeatherForecastResult retryWithDefaultLocation(String baseDate) {
        WeatherRequest defaultRetryWeatherRequest = locationErrorHandleWeatherRequest.ofBaseDate(baseDate);
        if (forecastCache.containsKey(DEFAULT_DISTRICT)) {
            Forecast forecast = forecastCache.get(DEFAULT_DISTRICT);
            return WeatherForecastResult.ofSuccessButLocationUpdate(forecast.getMinTemperature(),
                    forecast.getMaxTemperature());
        }
        return retryApiCallWithDefaultLocation(baseDate, defaultRetryWeatherRequest);
    }

    private WeatherForecastResult retryApiCallWithDefaultLocation(String baseDate,
                                                                  WeatherRequest defaultRetryWeatherRequest) {
        WeatherResponse weatherResponse = weatherApiCaller.getWeathers(defaultRetryWeatherRequest)
                .getResponse();
        KmaResponseCode responseCode = extractResultCode(weatherResponse);
        if (responseCode.isSuccessful()) {
            Forecast temperature = extractTemperatures(baseDate, weatherResponse.getBody());
            return WeatherForecastResult.ofSuccessButLocationUpdate(
                    temperature.getMinTemperature(), temperature.getMaxTemperature());
        }
        return WeatherForecastResult.ofFailure(responseCode.name(), responseCode.isRetryable());
    }

    private Forecast extractTemperatures(String baseDate, WeatherResponseBody body) {
        List<WeatherItem> temperatureForecasts = body.getItems()
                .getItem()
                .stream()
                .filter(kmaWeatherItem -> kmaWeatherItem.getFcstDate().equals(baseDate))
                .collect(Collectors.toUnmodifiableList());
        return temperatureForecastExtractor.extract(temperatureForecasts);
    }

    private KmaResponseCode extractResultCode(WeatherResponse kmaWeatherResponse) {
        String resultCode = kmaWeatherResponse.getHeader()
                .getResultCode();
        return KmaResponseCode.from(resultCode);
    }

    @Override
    public void finish() {
        forecastCache.clear();
    }
}
