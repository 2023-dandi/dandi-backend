package dandi.dandi.pushnotification.application.port.out.weather;

import dandi.dandi.member.domain.Location;
import java.time.LocalDate;

public interface WeatherForecastInfoManager {

    WeatherForecast getForecasts(LocalDate now, Location location);
}
