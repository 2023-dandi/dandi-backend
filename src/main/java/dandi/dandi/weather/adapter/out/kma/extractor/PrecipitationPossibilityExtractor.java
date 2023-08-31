package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.POP;

@Component
public class PrecipitationPossibilityExtractor implements WeatherExtractor {

    @Override
    public boolean hasCategoryCode(String code) {
        return POP.isSameCategory(code);
    }

    @Override
    public void setValue(Weather.WeatherBuilder weatherBuilder, String value) {
        int precipitationPossibility = Integer.parseInt(value);
        weatherBuilder.precipitationPossibility(precipitationPossibility);
    }
}
