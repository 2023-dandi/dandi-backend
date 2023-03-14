package dandi.dandi.post.domain;

public class Temperatures {

    private final double minTemperature;
    private final double maxTemperature;

    public Temperatures(double minTemperature, double maxTemperature) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }
}
