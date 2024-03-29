package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.adapter.out.kma.client.KmaWeatherApiCaller;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static dandi.dandi.weather.adapter.out.kma.KmaConstant.KMA_DATE_FORMATTER;
import static dandi.dandi.weather.adapter.out.kma.KmaConstant.KMA_TIME_FORMATTER;

@Component
public class KmaWeatherRequester implements WeatherRequester {

    private static final String READ_TIME_OUT = "Read timed out";
    private static final String CONNECTION_TIME_OUT = "connect timed out";
    private static final String DATE_TYPE = "JSON";
    private static final int ROW_NUM = 1500;

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
        WeatherResponses weatherResponses = getWeather(weatherRequest);
        if (!weatherResponses.isSuccessful()) {
            raiseException(weatherResponses, location);
        }
        return convertToWeathers(location.getId(), weatherResponses);
    }

    private WeatherResponses getWeather(WeatherRequest weatherRequest) {
        try {
            return weatherApiCaller.getWeathers(weatherRequest);
        } catch (RuntimeException e) {
            if (isTimeoutException(e)) {
                throw new WeatherRequestRetryableException(e.getCause().getMessage());
            }
            throw new WeatherRequestFatalException(e.getMessage());
        }
    }

    private boolean isTimeoutException(RuntimeException e) {
        Throwable cause = e.getCause();
        return Objects.nonNull(cause) &&
                cause.getClass().equals(SocketTimeoutException.class) && hasTimeoutMessage(cause);
    }

    private boolean hasTimeoutMessage(Throwable cause) {
        return cause.getMessage().equals(READ_TIME_OUT) || cause.getMessage().equals(CONNECTION_TIME_OUT);
    }

    private void raiseException(WeatherResponses weatherResponses, WeatherLocation location) {
        if (weatherResponses.isNoDataLocationError()) {
            throw WeatherRequestFatalException.noData(location.getX(), location.getY());
        } else if (weatherResponses.isRetryableError()) {
            throw new WeatherRequestRetryableException(weatherResponses.getResultMessage());
        }
        throw new WeatherRequestFatalException(weatherResponses.getResultMessage());
    }

    private Weathers convertToWeathers(long weatherLocationId, WeatherResponses weatherResponses) {
        List<WeatherItem> weatherItems = weatherResponses.getWeatherItems();
        try {
            List<Weather> weathers = weatherExtractors.extract(weatherItems);
            return new Weathers(weatherLocationId, weathers);
        } catch (RuntimeException exception) {
            throw new WeatherRequestFatalException(exception.getMessage());
        }
    }
}
