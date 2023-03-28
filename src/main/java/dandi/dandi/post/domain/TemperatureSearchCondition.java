package dandi.dandi.post.domain;

public class TemperatureSearchCondition {

    private final double minTemperatureMinSearchCondition;
    private final double minTemperatureMaxSearchCondition;
    private final double maxTemperatureMinSearchCondition;
    private final double maxTemperatureMaxSearchCondition;

    public TemperatureSearchCondition(double minTemperatureMinSearchCondition,
                                      double minTemperatureMaxSearchCondition,
                                      double maxTemperatureMinSearchCondition,
                                      double maxTemperatureMaxSearchCondition) {
        this.minTemperatureMinSearchCondition = minTemperatureMinSearchCondition;
        this.minTemperatureMaxSearchCondition = minTemperatureMaxSearchCondition;
        this.maxTemperatureMinSearchCondition = maxTemperatureMinSearchCondition;
        this.maxTemperatureMaxSearchCondition = maxTemperatureMaxSearchCondition;
    }

    public double getMinTemperatureMinSearchCondition() {
        return minTemperatureMinSearchCondition;
    }

    public double getMinTemperatureMaxSearchCondition() {
        return minTemperatureMaxSearchCondition;
    }

    public double getMaxTemperatureMinSearchCondition() {
        return maxTemperatureMinSearchCondition;
    }

    public double getMaxTemperatureMaxSearchCondition() {
        return maxTemperatureMaxSearchCondition;
    }
}
