package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.TMP;

@Component
public class TemperatureExtractor implements WeatherExtractor {

    @Override
    public boolean hasCategoryCode(String code) {
        return TMP.isSameCategory(code);
    }

    @Override
    public void setValue(Weather.WeatherBuilder weatherBuilder, String value) {
        int temperature = Integer.parseInt(value);
        weatherBuilder.temperature(temperature);
    }
}
