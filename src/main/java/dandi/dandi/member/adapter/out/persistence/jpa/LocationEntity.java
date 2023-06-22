package dandi.dandi.member.adapter.out.persistence.jpa;

import dandi.dandi.member.domain.District;
import dandi.dandi.member.domain.Location;
import javax.persistence.Embeddable;

@Embeddable
public class LocationEntity {

    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String town;

    private LocationEntity() {
    }

    public LocationEntity(double latitude, double longitude, District district) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = district.getCountry();
        this.city = district.getCity();
        this.town = district.getTown();
    }

    public Location toLocation() {
        return new Location(latitude, longitude, new District(country, city, town));
    }
}
