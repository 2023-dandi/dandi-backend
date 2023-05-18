package dandi.dandi.weather.application.port.out;

public enum WeatherForecastResultCode {

    SUCCESS,
    FAILURE,
    SUCCESS_BUT_LOCATION_UPDATE;

    public boolean isFailure() {
        return this == FAILURE;
    }

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
