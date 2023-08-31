package dandi.dandi.weather.application.port.out;

public class WeatherRequestRetryableException extends WeatherRequestException {

    public WeatherRequestRetryableException(String message) {
        super(message);
    }
}
