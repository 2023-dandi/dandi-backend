package dandi.dandi.member.adapter.out.persistence;

import dandi.dandi.member.domain.Location;
import javax.persistence.Embeddable;

@Embeddable
public class LocationJpaEntity {

    private double latitude;
    private double longitude;

    private LocationJpaEntity() {
    }

    public LocationJpaEntity(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location toLocation() {
        return new Location(latitude, longitude);
    }
}
