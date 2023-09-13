package dandi.dandi.member.domain;

import java.util.Objects;

public class Location {

    private static final Location INITIAL_LOCATION = Location.of(0.0, 0.0, District.getInitialDistrict());

    private final Coordinate coordinate;
    private final District district;

    private Location(Coordinate coordinate, District district) {
        this.coordinate = coordinate;
        this.district = district;
    }

    public static Location of(double latitude, double longitude, District district) {
        return new Location(new Coordinate(latitude, longitude), district);
    }

    public static Location initial() {
        return INITIAL_LOCATION;
    }

    public double getLatitude() {
        return coordinate.getLatitude();
    }

    public double getLongitude() {
        return coordinate.getLongitude();
    }

    public District getDistrict() {
        return district;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(coordinate, location.coordinate) && Objects.equals(district, location.district);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, district);
    }
}
