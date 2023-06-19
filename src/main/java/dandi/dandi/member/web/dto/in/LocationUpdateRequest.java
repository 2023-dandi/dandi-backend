package dandi.dandi.member.web.dto.in;

import dandi.dandi.member.application.port.in.LocationUpdateCommand;

public class LocationUpdateRequest {

    private Double latitude;
    private Double longitude;
    private String district;

    public LocationUpdateRequest() {
    }

    public LocationUpdateRequest(Double latitude, Double longitude, String district) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.district = district;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDistrict() {
        return district;
    }

    public LocationUpdateCommand toCommand() {
        return new LocationUpdateCommand(latitude, longitude, district);
    }
}
