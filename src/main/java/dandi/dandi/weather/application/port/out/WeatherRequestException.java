package dandi.dandi.weather.application.port.out;

import dandi.dandi.advice.ExternalServerException;

public class WeatherRequestException extends ExternalServerException {

    public WeatherRequestException(String message) {
        super(message);
    }
}
