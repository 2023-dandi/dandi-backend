package dandi.dandi.weather.adapter.out.kma;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.weather.adapter.out.kma.dto.Forecast;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class WeatherForecastResultCacheTest {

    private static final int CACHE_RANGE = 5;

    @DisplayName("캐시 범위의 날씨가 있는지 응답한다.")
    @ParameterizedTest
    @CsvSource({"-5, -5, true", "-6, -5, false", "6, 6, true", "6, 7, false"})
    void hasKey(int nx, int ny, boolean expected) {
        WeatherForecastResultCache weatherForecastResultCache = new WeatherForecastResultCache(CACHE_RANGE);
        weatherForecastResultCache.put(new Coordinate(0, 0), new Forecast(10, 15));
        weatherForecastResultCache.put(new Coordinate(1, 1), new Forecast(10, 15));
        Coordinate coordinate = new Coordinate(nx, ny);

        boolean actual = weatherForecastResultCache.hasKey(coordinate);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("캐시 가능 범위의 데이터를 조회할 수 있다.")
    @Test
    void get() {
        WeatherForecastResultCache weatherForecastResultCache = new WeatherForecastResultCache(CACHE_RANGE);
        Forecast forecast = new Forecast(10, 15);
        weatherForecastResultCache.put(new Coordinate(0, 0), forecast);
        Coordinate coordinate = new Coordinate(5, -5);

        Forecast actual = weatherForecastResultCache.get(coordinate);

        assertThat(actual).isEqualTo(forecast);
    }

    @DisplayName("캐시를 비울 수 있다.")
    @Test
    void clear() {
        WeatherForecastResultCache weatherForecastResultCache = new WeatherForecastResultCache(CACHE_RANGE);
        Coordinate coordinate = new Coordinate(0, 0);
        weatherForecastResultCache.put(coordinate, new Forecast(10, 15));

        weatherForecastResultCache.clear();

        assertThat(weatherForecastResultCache.get(coordinate)).isNull();
    }
}
