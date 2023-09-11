package dandi.dandi.weather.adapter.out.persistence.jpa;

import dandi.dandi.weather.application.port.out.WeatherPersistencePort;
import dandi.dandi.weather.domain.Weathers;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeatherPersistenceAdapter implements WeatherPersistencePort {

    private final WeatherRepository weatherRepository;

    public WeatherPersistenceAdapter(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Override
    public void save(List<Weathers> weathers) {
        List<WeatherJpaEntity> weatherJpaEntities = weathers.stream()
                .map(this::parseToEntity)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableList());
        weatherRepository.saveAll(weatherJpaEntities);
    }

    private List<WeatherJpaEntity> parseToEntity(Weathers weathers) {
        long weatherLocationId = weathers.getWeatherLocationId();
        return weathers.getValues()
                .stream()
                .map(weather -> WeatherJpaEntity.ofWeather(weather, weatherLocationId))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void deleteByLocationIds(List<Long> locationIds) {
        weatherRepository.deleteAllByWeatherLocationIds(locationIds);
    }
}
