package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.PCP;

@Component
public class PrecipitationAmountExtractor implements WeatherExtractor {

    private static final String BETWEEN_THIRTY_AND_FIFTY = "30.0~50.0mm";
    private static final String OVER_THAN_FIFTY = "50.0mm 이상";
    private static final String PRECIPITATION_AMOUNT_ZERO = "강수없음";
    private static final String PRECIPITATION_AMOUNT_UNIT = "mm";

    @Override
    public boolean hasCategoryCode(String code) {
        return PCP.isSameCategory(code);
    }

    @Override
    public void setValue(Weather.WeatherBuilder weatherBuilder, String value) {
        if (value.equals(PRECIPITATION_AMOUNT_ZERO)) {
            weatherBuilder.precipitationAmount(0.0);
        } else {
            double precipitationAmount = generatePrecipitationAmount(value);
            weatherBuilder.precipitationAmount(precipitationAmount);
        }
    }

    private double generatePrecipitationAmount(String value) {
        if (value.equals(BETWEEN_THIRTY_AND_FIFTY)) {
            return 40.0;
        } else if (value.equals(OVER_THAN_FIFTY)) {
            return 50.0;
        }
        String precipitationAmount = value.replace(PRECIPITATION_AMOUNT_UNIT, "");
        return Double.parseDouble(precipitationAmount);
    }
}
