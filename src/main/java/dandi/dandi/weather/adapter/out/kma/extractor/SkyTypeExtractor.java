package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.domain.Sky;
import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.SKY;

@Component
public class SkyTypeExtractor implements WeatherExtractor {

    @Override
    public boolean hasCategoryCode(String code) {
        return SKY.isSameCategory(code);
    }

    @Override
    public void setValue(Weather.WeatherBuilder weatherBuilder, String value) {
        Sky sky = generateSkyType(value);
        weatherBuilder.sky(sky);
    }

    private Sky generateSkyType(String value) {
        int skyCode = Integer.parseInt(value);
        if (skyCode == 1) {
            return Sky.SUNNY;
        } else if (skyCode == 3) {
            return Sky.CLOUDY;
        }
        return Sky.DARK;
    }
}
