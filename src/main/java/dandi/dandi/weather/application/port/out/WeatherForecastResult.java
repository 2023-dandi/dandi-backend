package dandi.dandi.weather.application.port.out;

import static dandi.dandi.weather.application.port.out.WeatherForecastResultCode.FAILURE;
import static dandi.dandi.weather.application.port.out.WeatherForecastResultCode.SUCCESS;
import static dandi.dandi.weather.application.port.out.WeatherForecastResultCode.SUCCESS_BUT_LOCATION_UPDATE;

import java.util.Objects;
import javax.annotation.Nullable;

public class WeatherForecastResult {

    private final WeatherForecastResultCode resultCode;
    @Nullable
    private final Integer minTemperature;
    @Nullable
    private final Integer maxTemperature;
    @Nullable
    private final String failureMessage;
    @Nullable
    private final Boolean retryableError;

    private WeatherForecastResult(WeatherForecastResultCode resultCode, Integer minTemperature,
                                  Integer maxTemperature, String failureMessage, Boolean retryableError) {
        this.resultCode = resultCode;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.failureMessage = failureMessage;
        this.retryableError = retryableError;
    }

    public static WeatherForecastResult ofFailure(String errorMessage, boolean retryableError) {
        return new WeatherForecastResult(FAILURE, null, null, errorMessage, retryableError);
    }

    public static WeatherForecastResult ofSuccess(int minTemperature, int maxTemperature) {
        return new WeatherForecastResult(SUCCESS, minTemperature, maxTemperature, null, null);
    }

    public static WeatherForecastResult ofSuccessButLocationUpdate(int minTemperature, int maxTemperature) {
        return new WeatherForecastResult(SUCCESS_BUT_LOCATION_UPDATE, minTemperature, maxTemperature, null, null);
    }

    public WeatherForecastResultCode getResultCode() {
        return resultCode;
    }

    @Nullable
    public Integer getMinTemperature() {
        return minTemperature;
    }

    @Nullable
    public Integer getMaxTemperature() {
        return maxTemperature;
    }

    @Nullable
    public String getFailureMessage() {
        return failureMessage;
    }

    public boolean isRetryableFailure() {
        return isFailed() && Objects.equals(retryableError, Boolean.TRUE);
    }

    public boolean isNonRetryableFailure() {
        return isFailed() && Objects.equals(retryableError, Boolean.FALSE);
    }

    public boolean isFailed() {
        return resultCode.isFailure();
    }

    public boolean isSuccess() {
        return resultCode.isSuccess();
    }
}
