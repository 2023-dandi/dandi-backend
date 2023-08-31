package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.domain.Weather;

public interface WeatherExtractor {

    boolean hasCategoryCode(String code);

    void setValue(Weather.WeatherBuilder weatherBuilder, String value);
}
