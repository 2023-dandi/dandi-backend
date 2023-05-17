package dandi.dandi.weather.application.port.out;

import javax.annotation.Nullable;

public class WeatherForecastResponse {

    private final WeatherForecastResultCode resultCode;
    @Nullable
    private final Integer minTemperature;
    @Nullable
    private final Integer maxTemperature;
    @Nullable
    private final String errorMessage;

    public WeatherForecastResponse(WeatherForecastResultCode resultCode, @Nullable Integer minTemperature,
                                   @Nullable Integer maxTemperature, @Nullable String errorMessage) {
        this.resultCode = resultCode;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.errorMessage = errorMessage;
    }

    public static WeatherForecastResponse ofFailure(String errorMessage) {
        return new WeatherForecastResponse(WeatherForecastResultCode.FAILURE, null, null, errorMessage);
    }

    public static WeatherForecastResponse ofSuccess(WeatherForecastResultCode resultCode,
                                                    int minTemperature, int maxTemperature) {
        return new WeatherForecastResponse(resultCode, minTemperature, maxTemperature, null);
    }

    public Integer getMinTemperature() {
        return minTemperature;
    }

    public Integer getMaxTemperature() {
        return maxTemperature;
    }

    public WeatherForecastResultCode getResultCode() {
        return resultCode;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isFailed() {
        return resultCode.isFailure();
    }

    public boolean isSuccess() {
        return resultCode.isSuccess();
    }
}
