package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.adapter.out.kma.dto.Forecast;
import dandi.dandi.weather.exception.InvalidKmaWeatherForecastCacheKeyException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeatherForecastResultCache {

    private final Map<Coordinate, Forecast> values = new ConcurrentHashMap<>();
    private final double cacheEnableDistance;

    public WeatherForecastResultCache(@Value("${weather.kma.cache-range}") int cacheEnableDistance) {
        this.cacheEnableDistance = Math.sqrt(Math.pow(cacheEnableDistance, 2) + Math.pow(cacheEnableDistance, 2));
    }

    public boolean hasKeyInRange(Coordinate coordinate) {
        return values.keySet()
                .stream()
                .anyMatch(cache -> coordinate.isCloserThan(cache, cacheEnableDistance));
    }

    public void put(Coordinate coordinate, Forecast forecast) {
        values.put(coordinate, forecast);
    }

    public Forecast get(Coordinate coordinate) {
        Coordinate cacheKey = values.keySet()
                .stream()
                .filter(cache -> coordinate.isCloserThan(cache, cacheEnableDistance))
                .findFirst()
                .orElseThrow(InvalidKmaWeatherForecastCacheKeyException::new);
        return values.get(cacheKey);
    }

    public void clear() {
        values.clear();
    }
}
