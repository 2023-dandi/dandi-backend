package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.PCP;

@Component
public class PrecipitationAmountExtractor implements WeatherExtractor {

    private static final String PRECIPITATION_AMOUNT_UNIT = "mm";

    @Override
    public boolean hasCategoryCode(String code) {
        return PCP.isSameCategory(code);
    }

    @Override
    public void setValue(Weather.WeatherBuilder weatherBuilder, String value) {
        double precipitationAmount = generatePrecipitationAmount(value);
        weatherBuilder.precipitationAmount(precipitationAmount);
    }

    private double generatePrecipitationAmount(String value) {
        String precipitationAmount = value.replace(PRECIPITATION_AMOUNT_UNIT, "");
        return Double.parseDouble(precipitationAmount);
    }
}
