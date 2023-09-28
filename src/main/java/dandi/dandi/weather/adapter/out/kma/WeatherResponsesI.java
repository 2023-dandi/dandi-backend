package dandi.dandi.weather.adapter.out.kma;

import java.util.List;

import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;

public interface WeatherResponsesI {

	boolean isSuccessful();

	boolean isRetryableError();

	boolean isNoDataLocationError();

	List<WeatherItem> getWeatherItems();

	String getResultMessage();
}
