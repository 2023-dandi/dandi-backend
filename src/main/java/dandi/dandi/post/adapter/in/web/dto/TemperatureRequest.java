package dandi.dandi.post.adapter.in.web.dto;

public class TemperatureRequest {

    private Double min;
    private Double max;

    public TemperatureRequest() {
    }

    public TemperatureRequest(Double min, Double max) {
        this.min = min;
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }
}
