package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.adapter.out.kma.code.KmaResponseCode;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherRequest;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponses;
import dandi.dandi.weather.adapter.out.kma.extractor.WeatherExtractors;
import dandi.dandi.weather.application.port.out.WeatherRequestException;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.application.port.out.WeatherRequestRetryableException;
import dandi.dandi.weather.application.port.out.WeatherRequester;
import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.WeatherLocation;
import dandi.dandi.weather.domain.Weathers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dandi.dandi.weather.adapter.out.kma.KmaConstant.KMA_DATE_FORMATTER;
import static dandi.dandi.weather.adapter.out.kma.KmaConstant.KMA_TIME_FORMATTER;

@Component
public class KmaWeatherRequester implements WeatherRequester {

    private static final String DATE_TYPE = "JSON";
    private static final int ROW_NUM = 900;

    private final KmaWeatherApiCaller weatherApiCaller;
    private final String kmaServiceKey;
    private final KmaBaseTimeConvertor kmaBaseTimeConvertor;
    private final WeatherExtractors weatherExtractors;
    private final Map<WeatherLocation, Weathers> cache = new ConcurrentHashMap<>();

    public KmaWeatherRequester(KmaWeatherApiCaller weatherApiCaller,
                               @Value("${weather.kma.service-key}") String kmaServiceKey,
                               KmaBaseTimeConvertor kmaBaseTimeConvertor,
                               WeatherExtractors weatherExtractors) {
        this.weatherApiCaller = weatherApiCaller;
        this.kmaServiceKey = kmaServiceKey;
        this.kmaBaseTimeConvertor = kmaBaseTimeConvertor;
        this.weatherExtractors = weatherExtractors;
    }

    public Weathers getWeathers(LocalDateTime now, WeatherLocation location) throws WeatherRequestException {
        if (cache.containsKey(location)) {
            return cache.get(location);
        }
        Weathers weathers = requestWeather(now, location);
        cache.put(location, weathers);
        return weathers;
    }

    private Weathers requestWeather(LocalDateTime now, WeatherLocation location) {
        LocalTime convertedBaseTime = kmaBaseTimeConvertor.convert(now.toLocalTime());
        String baseTime = convertedBaseTime.format(KMA_TIME_FORMATTER);
        String baseDate = now.format(KMA_DATE_FORMATTER);
        WeatherRequest weatherRequest = new WeatherRequest(
                kmaServiceKey, DATE_TYPE, baseDate, baseTime, ROW_NUM, location.getX(), location.getY());
        WeatherResponses weatherResponses = weatherApiCaller.getWeathers(weatherRequest);
        KmaResponseCode kmaResponseCode = extractResponseCode(weatherResponses);
        if (!kmaResponseCode.isSuccessful()) {
            raiseException(kmaResponseCode, location);
        }
        return convertToWeathers(location.getId(), weatherResponses);
    }

    private void raiseException(KmaResponseCode kmaResponseCode, WeatherLocation location) {
        if (kmaResponseCode.isErrorAssociatedWithLocation()) {
            throw WeatherRequestFatalException.noData(location.getX(), location.getY());
        } else if (kmaResponseCode.isTemporaryExternalServerError()) {
            throw new WeatherRequestRetryableException(kmaResponseCode.name());
        }
        throw new WeatherRequestFatalException(kmaResponseCode.name());
    }

    private KmaResponseCode extractResponseCode(WeatherResponses weatherResponses) {
        String resultCode = weatherResponses.getResponse()
                .getHeader()
                .getResultCode();
        return KmaResponseCode.from(resultCode);
    }

    private Weathers convertToWeathers(long weatherLocationId, WeatherResponses weatherResponses) {
        List<WeatherItem> weatherItems = weatherResponses.getResponse()
                .getBody()
                .getItems()
                .getItem();
        try {
            List<Weather> weathers = weatherExtractors.extract(weatherItems);
            return new Weathers(weatherLocationId, weathers);
        } catch (RuntimeException exception) {
            throw new WeatherRequestFatalException(exception.getMessage());
        }
    }

    public void finish() {
        cache.clear();
    }
}
