package dandi.dandi.weather.adapter.out.persistence.jpa;

import dandi.dandi.weather.application.port.out.WeatherPersistencePort;
import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.Weathers;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WeatherPersistenceAdapter implements WeatherPersistencePort {

    private final WeatherRepository weatherRepository;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public WeatherPersistenceAdapter(WeatherRepository weatherRepository, DataSource dataSource) {
        this.weatherRepository = weatherRepository;
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("WEATHER")
                .usingGeneratedKeyColumns("weather_id");
    }

    @Override
    public void saveInBatch(List<Weathers> weathers) {
        simpleJdbcInsert.executeBatch(generateSqlParameterSources(weathers));
    }

    private SqlParameterSource[] generateSqlParameterSources(List<Weathers> weathers) {
        return weathers.stream()
                .map(this::generate)
                .flatMap(Arrays::stream)
                .toArray(SqlParameterSource[]::new);
    }

    private MapSqlParameterSource[] generate(Weathers a) {
        long weatherLocationId = a.getWeatherLocationId();
        return a.getValues()
                .stream()
                .map(weather -> generateMapSqlParameterSource(weatherLocationId, weather))
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource generateMapSqlParameterSource(long weatherLocationId, Weather weather) {
        Map<String, Object> values = new HashMap<>(Map.of(
                "date_time", weather.getDateTime(),
                "forecasted_at", weather.getForecastedAt(),
                "humidity", weather.getHumidity(),
                "precipitation_amount", weather.getPrecipitationAmount(),
                "precipitation_possibility", weather.getPrecipitationPossibility(),
                "precipitation_type", weather.getPrecipitationType().name(),
                "sky", weather.getSky().name(),
                "temperature", weather.getTemperature(),
                "wind_direction", weather.getWindDirection().name(),
                "wind_speed", weather.getWindSpeed()));
        values.put("weather_location_id", weatherLocationId);
        return new MapSqlParameterSource()
                .addValues(values);
    }

    @Override
    public void deleteByLocationIds(List<Long> locationIds) {
        weatherRepository.deleteAllByWeatherLocationIds(locationIds);
    }
}
