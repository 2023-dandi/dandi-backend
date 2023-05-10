package dandi.dandi.pushnotification.adapter.out.weather.kma;

import dandi.dandi.pushnotification.application.port.out.weather.Temperature;
import dandi.dandi.pushnotification.application.port.out.weather.WeatherForecastInfoManager;
import java.time.LocalDateTime;
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
    private static final int ROW_COUNT = 300;

    private final KmaWeatherApiCaller weatherApiCaller;
    private final String kmaServiceKey;
    private final KmaCoordinateConvertor kmaCoordinateConvertor;
    private final TemperatureForecastExtractor temperatureForecastExtractor;

    public KmaTemperatureForecastManager(KmaWeatherApiCaller weatherApiCaller,
                                         @Value("${weather.kma.service-key}") String kmaServiceKey,
                                         KmaCoordinateConvertor kmaCoordinateConvertor,
                                         TemperatureForecastExtractor temperatureForecastExtractor) {
        this.weatherApiCaller = weatherApiCaller;
        this.kmaServiceKey = kmaServiceKey;
        this.kmaCoordinateConvertor = kmaCoordinateConvertor;
        this.temperatureForecastExtractor = temperatureForecastExtractor;
    }

    public Temperature getForecasts(LocalDateTime now, double latitude, double longitude) {
        String baseDate = now.format(KMA_DATE_FORMATTER);
        Coordinate coordinate = kmaCoordinateConvertor.convert(latitude, longitude);
        WeatherRequest weatherRequest = new WeatherRequest(
                kmaServiceKey, DATA_TYPE, baseDate, BASE_TIME, ROW_COUNT, coordinate.getNx(), coordinate.getNy());
        List<WeatherItem> todayWeatherForecast = requestTodayWeatherForecast(baseDate, weatherRequest);
        return temperatureForecastExtractor.extract(todayWeatherForecast);
    }

    private List<WeatherItem> requestTodayWeatherForecast(String baseDate, WeatherRequest weatherRequest) {
        return weatherApiCaller.getWeathers(weatherRequest)
                .getResponse()
                .getBody()
                .getItems()
                .getItem()
                .stream()
                .filter(weatherItem -> weatherItem.getFcstDate().equals(baseDate))
                .collect(Collectors.toUnmodifiableList());
    }
}
