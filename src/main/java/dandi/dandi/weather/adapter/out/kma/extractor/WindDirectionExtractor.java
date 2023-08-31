package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.adapter.out.kma.code.KmaWindDirection;
import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.WindDirection;
import org.springframework.stereotype.Component;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.VEC;

@Component
public class WindDirectionExtractor implements WeatherExtractor {

    private static final double ADDITION_DATA = 11.25;
    private static final double DIVISION_DATA = 22.5;

    @Override
    public boolean hasCategoryCode(String code) {
        return VEC.isSameCategory(code);
    }

    @Override
    public void setValue(Weather.WeatherBuilder weatherBuilder, String value) {
        int windDirectionValue = (int) ((Double.parseDouble(value) + ADDITION_DATA) / DIVISION_DATA);
        WindDirection windDirection = KmaWindDirection.from(windDirectionValue)
                .getWindDirection();
        weatherBuilder.windDirection(windDirection);
    }
}
