package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.adapter.out.kma.dto.Forecast;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeatherForecastResultCache {

    private final Map<Coordinate, Forecast> values = new HashMap<>();
    private final int cacheRange;

    public WeatherForecastResultCache(@Value("${weather.kma.cache-range}") int cacheRange) {
        this.cacheRange = cacheRange;
    }

    public boolean hasKey(Coordinate coordinate) {
        return values.keySet()
                .stream()
                .anyMatch(cache -> coordinate.isInRange(cache, cacheRange));
    }

    public void put(Coordinate coordinate, Forecast forecast) {
        values.put(coordinate, forecast);
    }

    public Forecast get(Coordinate coordinate) {
        Coordinate cacheKey = values.keySet()
                .stream()
                .filter(cache -> coordinate.isInRange(cache, cacheRange))
                .findFirst()
                .orElseThrow();
        return values.get(cacheKey);
    }

    public void clear() {
        values.clear();
    }
}
