package dandi.dandi.post.application.port.in;

public class TemperatureResponse {

    private double min;
    private double max;

    public TemperatureResponse() {
    }

    public TemperatureResponse(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
