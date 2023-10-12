package dandi.dandi.weather.adapter.out.persistence.jpa;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class LocationAndDateTime implements Serializable {

    private long weatherLocationId;
    private LocalDateTime dateTime;

    protected LocationAndDateTime() {
    }

    public LocationAndDateTime(long weatherLocationId, LocalDateTime dateTime) {
        this.weatherLocationId = weatherLocationId;
        this.dateTime = dateTime;
    }

    public long getWeatherLocationId() {
        return weatherLocationId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationAndDateTime)) return false;
        LocationAndDateTime that = (LocationAndDateTime) o;
        return weatherLocationId == that.weatherLocationId && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weatherLocationId, dateTime);
    }
}
