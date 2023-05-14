package dandi.dandi.weather.application.port.out;

public class WeatherForecast {

    private final int minTemperature;
    private final int maxTemperature;

    public WeatherForecast(int minTemperature, int maxTemperature) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }
}