package dandi.dandi.weather.application.port.out;

import dandi.dandi.weather.domain.WeatherLocation;
import dandi.dandi.weather.domain.Weathers;

import java.time.LocalDateTime;

public interface WeatherRequester {

    Weathers getWeathers(LocalDateTime baseDateTime, WeatherLocation location) throws WeatherRequestException;
}
