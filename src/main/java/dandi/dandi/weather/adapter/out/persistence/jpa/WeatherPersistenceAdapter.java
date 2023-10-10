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
import java.util.List;

@Component
public class WeatherPersistenceAdapter implements WeatherPersistencePort {

    private final WeatherRepository weatherRepository;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public WeatherPersistenceAdapter(WeatherRepository weatherRepository, DataSource dataSource) {
        this.weatherRepository = weatherRepository;
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("weather")
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
        return new MapSqlParameterSource()
                .addValue("date_time", weather.getDateTime())
                .addValue("forecasted_at", weather.getForecastedAt())
                .addValue("humidity", weather.getHumidity())
                .addValue("precipitation_amount", weather.getPrecipitationAmount())
                .addValue("precipitation_possibility", weather.getPrecipitationPossibility())
                .addValue("precipitation_type", weather.getPrecipitationType().name())
                .addValue("sky", weather.getSky().name())
                .addValue("temperature", weather.getTemperature())
                .addValue("wind_direction", weather.getWindDirection().name())
                .addValue("wind_speed", weather.getWindSpeed())
                .addValue("weather_location_id", weatherLocationId);
    }

    @Override
    public void deleteByLocationIds(List<Long> locationIds) {
        weatherRepository.deleteAllByWeatherLocationIds(locationIds);
    }
}
