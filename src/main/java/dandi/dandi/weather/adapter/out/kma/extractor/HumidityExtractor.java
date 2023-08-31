package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.REH;

@Component
public class HumidityExtractor implements WeatherExtractor {

    @Override
    public boolean hasCategoryCode(String code) {
        return REH.isSameCategory(code);
    }

    @Override
    public void setValue(Weather.WeatherBuilder weatherBuilder, String value) {
        int humidity = Integer.parseInt(value);
        weatherBuilder.humidity(humidity);
    }
}
