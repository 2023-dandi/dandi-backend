package dandi.dandi.weather.exception;

public class InvalidKmaWeatherForecastCacheKeyException extends RuntimeException {

    private static final String MESSAGE = "좌표에 해당 하는 캐시 값이 존재하지 않습니다.";

    public InvalidKmaWeatherForecastCacheKeyException() {
        super(MESSAGE);
    }
}
