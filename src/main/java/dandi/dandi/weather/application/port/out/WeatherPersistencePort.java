package dandi.dandi.weather.application.port.out;

import dandi.dandi.weather.domain.Weathers;

import java.util.List;

public interface WeatherPersistencePort {

    void save(List<Weathers> weathers);
}
