package dandi.dandi.member.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Location {

    private static final Location INITIAL_LOCATION = new Location(0.0, 0.0);

    @Embedded
    private Latitude latitude;

    @Embedded
    private Longitude longitude;

    private Location() {
    }

    private Location(Latitude latitude, Longitude longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(double latitude, double longitude) {
        this(new Latitude(latitude), new Longitude(longitude));
    }

    public static Location initial() {
        return INITIAL_LOCATION;
    }

    public double getLatitude() {
        return latitude.getValue();
    }

    public double getLongitude() {
        return longitude.getValue();
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
        return Objects.equals(latitude, location.latitude) && Objects.equals(longitude,
                location.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
