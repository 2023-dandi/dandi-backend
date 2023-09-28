package dandi.dandi.weather.adapter.out.kma.dto;

public class WeatherResponse {

    private WeatherResponseHeader header;
    private WeatherResponseBody body;

    public WeatherResponse() {
    }

    public WeatherResponse(WeatherResponseHeader header, WeatherResponseBody body) {
        this.header = header;
        this.body = body;
    }

    public WeatherResponseHeader getHeader() {
        return header;
    }

    public WeatherResponseBody getBody() {
        return body;
    }

    public boolean isSuccessful() {
        return header.isSuccessful();
    }

    public boolean isRetryableError() {
        return header.isRetryableError();
    }

    public boolean isNoDataLocationError() {
        return header.isNoDataLocationError();
    }

    public String getResultMessage() {
        return header.getResultMessage();
    }
}
