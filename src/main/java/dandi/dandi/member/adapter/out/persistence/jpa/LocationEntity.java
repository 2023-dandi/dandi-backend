package dandi.dandi.member.adapter.out.persistence.jpa;

import dandi.dandi.member.domain.District;
import dandi.dandi.member.domain.Location;
import javax.persistence.Embeddable;

@Embeddable
public class LocationEntity {

    private double latitude;
    private double longitude;
    private String firstDistrict;
    private String secondDistrict;
    private String thirdDistrict;

    private LocationEntity() {
    }

    public LocationEntity(double latitude, double longitude, District district) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.firstDistrict = district.getFirst();
        this.secondDistrict = district.getSecond();
        this.thirdDistrict = district.getThird();
    }

    public Location toLocation() {
        return Location.of(latitude, longitude, new District(firstDistrict, secondDistrict, thirdDistrict));
    }
}
