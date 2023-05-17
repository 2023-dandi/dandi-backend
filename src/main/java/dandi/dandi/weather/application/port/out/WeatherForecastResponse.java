package dandi.dandi.weather.application.port.out;

import static dandi.dandi.weather.application.port.out.WeatherForecastResultCode.FAILURE;
import static dandi.dandi.weather.application.port.out.WeatherForecastResultCode.SUCCESS;
import static dandi.dandi.weather.application.port.out.WeatherForecastResultCode.SUCCESS_BUT_LOCATION_UPDATE;

import java.util.Objects;
import javax.annotation.Nullable;

public class WeatherForecastResponse {

    private final WeatherForecastResultCode resultCode;
    @Nullable
    private final Integer minTemperature;
    @Nullable
    private final Integer maxTemperature;
    @Nullable
    private final String errorMessage;
    @Nullable
    private final Boolean retryableError;

    private WeatherForecastResponse(WeatherForecastResultCode resultCode, Integer minTemperature,
                                    Integer maxTemperature, String errorMessage, Boolean retryableError) {
        this.resultCode = resultCode;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.errorMessage = errorMessage;
        this.retryableError = retryableError;
    }

    public static WeatherForecastResponse ofFailure(String errorMessage, boolean retryableError) {
        return new WeatherForecastResponse(FAILURE, null, null, errorMessage, retryableError);
    }

    public static WeatherForecastResponse ofSuccess(int minTemperature, int maxTemperature) {
        return new WeatherForecastResponse(SUCCESS, minTemperature, maxTemperature, null, null);
    }

    public static WeatherForecastResponse ofSuccessButLocationUpdate(int minTemperature, int maxTemperature) {
        return new WeatherForecastResponse(SUCCESS_BUT_LOCATION_UPDATE, minTemperature, maxTemperature, null, null);
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
    public String getErrorMessage() {
        return errorMessage;
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
