package dandi.dandi.batch.weather;

import dandi.dandi.weather.application.port.out.WeatherRequestException;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.application.port.out.WeatherRequestRetryableException;
import dandi.dandi.weather.application.port.out.WeatherRequester;
import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.WeatherLocation;
import dandi.dandi.weather.domain.Weathers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static dandi.dandi.weather.domain.PrecipitationType.RAIN;
import static dandi.dandi.weather.domain.Sky.CLOUDY;
import static dandi.dandi.weather.domain.WindDirection.SE;

@Configuration
public class WeatherBatchTestConfig {

    @Bean
    public WeatherRequester kmaWeatherRequester() {
        return new WeatherRequester() {

            private int requestCount = 0;

            @Override
            public Weathers getWeathers(LocalDateTime now, WeatherLocation location) throws WeatherRequestException {
                if (now.getYear() == 2020) {
                    throw WeatherRequestFatalException.noData(location.getLatitude(), location.getLongitude());
                } else if (now.getYear() == 2021) {
                    if (requestCount < 1) {
                        requestCount++;
                        throw new WeatherRequestRetryableException("DB_ERROR");
                    }
                    List<Weather> weathers = generateWeathers();
                    return new Weathers(1L, weathers);
                } else if (now.getYear() == 2022) {
                    throw new WeatherRequestRetryableException("DB_ERROR");
                }
                List<Weather> weathers = generateWeathers();
                return new Weathers(1L, weathers);
            }

            @Override
            public void finish() {
            }
        };
    }

    private List<Weather> generateWeathers() {
        List<Weather> weathers = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            Weather weather = new Weather.WeatherBuilder(LocalDate.of(2023, 8, 25), LocalTime.of(5, 0))
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
