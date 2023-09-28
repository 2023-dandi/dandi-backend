package dandi.dandi.weather.adapter.out.kma;

import static dandi.dandi.weather.adapter.out.kma.KmaConstant.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherRequest;
import dandi.dandi.weather.adapter.out.kma.extractor.WeatherExtractors;
import dandi.dandi.weather.application.port.out.WeatherRequestException;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.application.port.out.WeatherRequestRetryableException;
import dandi.dandi.weather.application.port.out.WeatherRequester;
import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.WeatherLocation;
import dandi.dandi.weather.domain.Weathers;

@Component
public class KmaWeatherRequester implements WeatherRequester {

    private static final String DATE_TYPE = "JSON";
    private static final int ROW_NUM = 900;

    private final KmaWeatherApiCaller weatherApiCaller;
    private final String kmaServiceKey;
    private final WeatherExtractors weatherExtractors;

    public KmaWeatherRequester(KmaWeatherApiCaller weatherApiCaller,
                               @Value("${weather.kma.service-key}") String kmaServiceKey,
                               WeatherExtractors weatherExtractors) {
        this.weatherApiCaller = weatherApiCaller;
        this.kmaServiceKey = kmaServiceKey;
        this.weatherExtractors = weatherExtractors;
    }

    public Weathers getWeathers(LocalDateTime baseDateTime, WeatherLocation location) throws WeatherRequestException {
        String date = baseDateTime.format(KMA_DATE_FORMATTER);
        String time = baseDateTime.format(KMA_TIME_FORMATTER);
        WeatherRequest weatherRequest = new WeatherRequest(
                kmaServiceKey, DATE_TYPE, date, time, ROW_NUM, location.getX(), location.getY());
        WeatherResponsesI weatherResponses = weatherApiCaller.getWeathers(weatherRequest);
        if (!weatherResponses.isSuccessful()) {
            raiseException(weatherResponses, location);
        }
        return convertToWeathers(location.getId(), weatherResponses);
    }

    private void raiseException(WeatherResponsesI weatherResponses, WeatherLocation location) {
        if (weatherResponses.isNoDataLocationError()) {
            throw WeatherRequestFatalException.noData(location.getX(), location.getY());
        } else if (weatherResponses.isRetryableError()) {
            throw new WeatherRequestRetryableException(weatherResponses.getResultMessage());
        }
        throw new WeatherRequestFatalException(weatherResponses.getResultMessage());
    }

    private Weathers convertToWeathers(long weatherLocationId, WeatherResponsesI weatherResponses) {
        List<WeatherItem> weatherItems = weatherResponses.getWeatherItems();
        try {
            List<Weather> weathers = weatherExtractors.extract(weatherItems);
            return new Weathers(weatherLocationId, weathers);
        } catch (RuntimeException exception) {
            throw new WeatherRequestFatalException(exception.getMessage());
        }
    }
}
