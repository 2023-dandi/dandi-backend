package dandi.dandi.weather.adapter.out.kma;

public class TemperatureDto {

    private final int minTemperature;
    private final int maxTemperature;

    public TemperatureDto(int minTemperature, int maxTemperature) {
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
