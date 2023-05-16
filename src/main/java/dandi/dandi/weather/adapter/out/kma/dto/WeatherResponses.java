package dandi.dandi.weather.adapter.out.kma.dto;

public class WeatherResponses {

    private WeatherResponse response;

    public WeatherResponses() {
    }

    public WeatherResponses(WeatherResponse response) {
        this.response = response;
    }

    public WeatherResponse getResponse() {
        return response;
    }
}
