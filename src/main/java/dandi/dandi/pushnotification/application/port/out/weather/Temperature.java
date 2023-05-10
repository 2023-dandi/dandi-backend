package dandi.dandi.pushnotification.application.port.out.weather;

public class Temperature {

    private final int minTemperature;
    private final int maxTemperature;

    public Temperature(int minTemperature, int maxTemperature) {
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
