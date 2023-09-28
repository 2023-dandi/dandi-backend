package dandi.dandi.weather.adapter.out.kma.dto;

import java.util.List;

import dandi.dandi.weather.adapter.out.kma.WeatherResponses;

public class KmaWeatherResponses implements WeatherResponses {

    private WeatherResponse response;

    public KmaWeatherResponses() {
    }

    public KmaWeatherResponses(WeatherResponse response) {
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
