package dandi.dandi.weather.adapter.out.kma.dto;

import java.util.List;

import dandi.dandi.weather.adapter.out.kma.WeatherResponsesI;

public class WeatherResponses implements WeatherResponsesI {

    private WeatherResponse response;

    public WeatherResponses() {
    }

    public WeatherResponses(WeatherResponse response) {
        this.response = response;
    }

    public WeatherResponse getResponse() {
        return response;
    }

    @Override
    public boolean isSuccessful() {
        return response.isSuccessful();
    }

    @Override
    public boolean isRetryableError() {
        return response.isRetryableError();
    }

    @Override
    public boolean isNoDataLocationError() {
        return response.isNoDataLocationError();
    }

    @Override
    public List<WeatherItem> getWeatherItems() {
        return response.getBody()
            .getItems()
            .getItem();
    }

    @Override
    public String getResultMessage() {
        return response.getResultMessage();
    }
}
