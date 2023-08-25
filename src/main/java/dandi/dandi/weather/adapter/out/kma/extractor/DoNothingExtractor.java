package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.*;

@Component
public class DoNothingExtractor implements WeatherExtractor {

    @Override
    public boolean hasCategoryCode(String code) {
        return SNO.isSameCategory(code) || TMN.isSameCategory(code) || TMX.isSameCategory(code) ||
                UUU.isSameCategory(code) || VVV.isSameCategory(code) || WAV.isSameCategory(code);
    }

    @Override
    public void setValue(Weather.WeatherBuilder weatherBuilder, String value) {
        // this is For Data of Category that is useless in our service
    }
}
