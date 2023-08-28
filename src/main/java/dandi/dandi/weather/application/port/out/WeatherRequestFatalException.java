package dandi.dandi.weather.application.port.out;

public class WeatherRequestFatalException extends WeatherRequestException {

    private static final String NO_DATA_EXCEPTION_MESSAGE = "위치에 대한 날씨 정보가 존재하지 않습니다.(%f, %f)";

    public WeatherRequestFatalException(String message) {
        super(message);
    }

    public static WeatherRequestFatalException noData(double latitude, double longitude) {
        return new WeatherRequestFatalException(String.format(NO_DATA_EXCEPTION_MESSAGE, latitude, longitude));
    }
}
