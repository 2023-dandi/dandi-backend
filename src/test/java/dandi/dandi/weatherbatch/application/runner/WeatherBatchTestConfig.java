package dandi.dandi.weatherbatch.application.runner;

import dandi.dandi.weather.application.port.out.WeatherRequestException;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.application.port.out.WeatherRequestRetryableException;
import dandi.dandi.weather.application.port.out.WeatherRequester;
import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.WeatherLocation;
import dandi.dandi.weather.domain.Weathers;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static dandi.dandi.weather.domain.PrecipitationType.RAIN;
import static dandi.dandi.weather.domain.Sky.CLOUDY;
import static dandi.dandi.weather.domain.WindDirection.SE;

public class WeatherBatchTestConfig {

    @Bean
    public WeatherRequester mockWeatherRequester() {
        return new WeatherRequester() {

            private int requestCount = 1;

            @Override
            public Weathers getWeathers(LocalDateTime now, WeatherLocation location) throws WeatherRequestException {
                if (now.getYear() == 2020) {
                    throw WeatherRequestFatalException.noData(location.getX(), location.getY());
                } else if (now.getYear() == 2021) {
                    if (requestCount < 2) {
                        requestCount++;
                        throw new WeatherRequestRetryableException("DB_ERROR");
                    }
                    List<Weather> weathers = generateWeathers();
                    return new Weathers(requestCount, weathers);
                } else if (now.getYear() == 2022) {
                    throw new WeatherRequestRetryableException("DB_ERROR");
                }
                List<Weather> weathers = generateWeathers();
                return new Weathers(1L, weathers);
            }
        };
    }

    private List<Weather> generateWeathers() {
        List<Weather> weathers = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            Weather weather = new Weather.WeatherBuilder(LocalDateTime.of(2023, 8, 25, i, 0))
                    .temperature(15.0)
                    .precipitationPossibility(60)
                    .precipitationType(RAIN)
                    .precipitationAmount(1.0)
                    .humidity(70)
                    .windDirection(SE)
                    .windSpeed(2.0)
                    .sky(CLOUDY)
                    .build();
            weathers.add(weather);
        }
        return weathers;
    }
}
