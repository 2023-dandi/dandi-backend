package dandi.dandi.pushnotification.application.port.out.weather;

import java.time.LocalDateTime;

public interface WeatherForecastInfoManager {

    Temperature getForecasts(LocalDateTime now, double latitude, double longitude);
}
