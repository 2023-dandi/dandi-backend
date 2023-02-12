package dandi.dandi.member.domain;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Location {

    private static final Location INITIAL_LOCATION = new Location(0.0, 0.0);

    private double latitude;
    private double longitude;

    private Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private Location() {
    }

    public static Location initial() {
        return INITIAL_LOCATION;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        Location location = (Location) o;
        return Double.compare(location.latitude, latitude) == 0
                && Double.compare(location.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
