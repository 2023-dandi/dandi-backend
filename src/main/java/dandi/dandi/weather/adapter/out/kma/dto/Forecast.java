package dandi.dandi.weather.adapter.out.kma.dto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Forecast)) {
            return false;
        }
        Forecast forecast = (Forecast) o;
        return minTemperature == forecast.minTemperature && maxTemperature == forecast.maxTemperature;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minTemperature, maxTemperature);
    }
}
