package dandi.dandi.member.web.dto.in;

import dandi.dandi.member.application.port.in.LocationUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;

public class LocationUpdateRequest {

    private Double latitude;
    private Double longitude;

    public LocationUpdateRequest() {
    }

    public LocationUpdateRequest(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LocationUpdateCommand toCommand() {
        return new LocationUpdateCommand(latitude, longitude);
    }
}
