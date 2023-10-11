package dandi.dandi.weather.adapter.out.persistence.jpa;

import dandi.dandi.common.PersistenceAdapterTest;
import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.Weathers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static dandi.dandi.weather.domain.PrecipitationType.RAIN;
import static dandi.dandi.weather.domain.Sky.CLOUDY;
import static dandi.dandi.weather.domain.WindDirection.SE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WeatherPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private WeatherPersistenceAdapter weatherPersistenceAdapter;

    @Test
    void saveInBatch() {
        List<Weather> weatherItems = generateWeathers();
        List<Weathers> weathers = List.of(new Weathers(1L, weatherItems), new Weathers(2L, weatherItems), new Weathers(3L, weatherItems));

        weatherPersistenceAdapter.saveInBatch(weathers);

        assertThat(weatherRepository.findAll()).hasSize(72);
    }

    private List<Weather> generateWeathers() {
        List<Weather> weathers = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            Weather weather = new Weather.WeatherBuilder(LocalDateTime.of(2023, 8, 25, i, 0))
                    .temperature(15.0)
                    .precipitationPossibility(60)
                    .precipitationType(RAIN)
                    .precipitationAmount(i)
                    .forecastedAt(LocalDateTime.now())
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
