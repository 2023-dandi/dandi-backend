package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.adapter.out.kma.code.KmaPrecipitationType;
import dandi.dandi.weather.domain.PrecipitationType;
import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.PTY;

@Component
public class PrecipitationTypeExtractor implements WeatherExtractor {

    @Override
    public boolean hasCategoryCode(String code) {
        return PTY.isSameCategory(code);
    }

    @Override
    public void setValue(Weather.WeatherBuilder weatherBuilder, String value) {
        PrecipitationType precipitationType = generatePrecipitationType(value);
        weatherBuilder.precipitationType(precipitationType);
    }

    private PrecipitationType generatePrecipitationType(String value) {
        int precipitationTypeCode = Integer.parseInt(value);
        KmaPrecipitationType kmaPrecipitationType = KmaPrecipitationType.from(precipitationTypeCode);
        return kmaPrecipitationType.getPrecipitationType();
    }
}
