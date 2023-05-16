package dandi.dandi.weather.adapter.out.kma;

public class WeatherResponse {

    private WeatherResponseHeader header;
    private WeatherResponseBody body;

    public WeatherResponse() {
    }

    public WeatherResponseHeader getHeader() {
        return header;
    }

    public WeatherResponseBody getBody() {
        return body;
    }
}
