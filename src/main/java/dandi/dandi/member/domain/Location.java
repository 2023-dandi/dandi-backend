package dandi.dandi.member.domain;

import java.util.Objects;

public class Location {

    private static final Location INITIAL_LOCATION = new Location(0.0, 0.0, District.getInitialDistrict());

    private final double latitude;
    private final double longitude;
    private final District district;

    public Location(double latitude, double longitude, District district) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.district = district;
    }

    public static Location initial() {
        return INITIAL_LOCATION;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public District getDistrict() {
        return district;
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
