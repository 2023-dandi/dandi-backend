package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.WSD;

@Component
public class WindSpeedExtractor implements WeatherExtractor {

    @Override
    public boolean hasCategoryCode(String code) {
        return WSD.isSameCategory(code);
    }

    @Override
    public void setValue(Weather.WeatherBuilder weatherBuilder, String value) {
        double windSpeed = Double.parseDouble(value);
        weatherBuilder.windSpeed(windSpeed);
    }
}
