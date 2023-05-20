package dandi.dandi.member.application.port.in;

import dandi.dandi.common.validation.SelfValidating;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LocationUpdateCommand extends SelfValidating<LocationUpdateCommand> {

    private static final String INVALID_LOCATION_EXCEPTION_MESSAGE =
            "위도, 경도 값이 존재하지 않거나 범위가 잘못되었고 지역 문자열이 존재하지 않습니다.";

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private final Double latitude;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private final Double longitude;

    @NotNull
    @NotBlank
    private final String district;

    public LocationUpdateCommand(Double latitude, Double longitude, String district) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.district = district;
        this.validateSelf(INVALID_LOCATION_EXCEPTION_MESSAGE);
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
