package dandi.dandi.weather.adapter.out.kma.dto;

public class Forecast {

    private final int minTemperature;
    private final int maxTemperature;

    public Forecast(int minTemperature, int maxTemperature) {
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
