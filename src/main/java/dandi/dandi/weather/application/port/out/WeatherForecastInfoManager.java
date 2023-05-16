package dandi.dandi.weather.application.port.out;

import dandi.dandi.member.domain.Location;
import java.time.LocalDate;

public interface WeatherForecastInfoManager {

    WeatherForecastResponse getForecasts(LocalDate now, Location location);
}
