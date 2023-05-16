package dandi.dandi.weather.application.port.out;

public class WeatherForecastResponse {

    private final boolean success;
    private final int minTemperature;
    private final int maxTemperature;

    public WeatherForecastResponse(boolean success, int minTemperature, int maxTemperature) {
        this.success = success;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public static WeatherForecastResponse ofSuccess(int minTemperature, int maxTemperature) {
        return new WeatherForecastResponse(true, minTemperature, maxTemperature);
    }

    public static WeatherForecastResponse ofFailure() {
        return new WeatherForecastResponse(false, 0, 0);
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public boolean isSuccessful() {
        return success;
    }
}
