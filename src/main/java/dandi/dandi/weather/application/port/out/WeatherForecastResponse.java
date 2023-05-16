package dandi.dandi.weather.application.port.out;

public class WeatherForecastResponse {

    private final WeatherForecastResultCode resultCode;
    private final int minTemperature;
    private final int maxTemperature;

    public WeatherForecastResponse(WeatherForecastResultCode resultCode, int minTemperature, int maxTemperature) {
        this.resultCode = resultCode;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public static WeatherForecastResponse ofFailure() {
        return new WeatherForecastResponse(WeatherForecastResultCode.FAILURE, 0, 0);
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public WeatherForecastResultCode getResultCode() {
        return resultCode;
    }
}
