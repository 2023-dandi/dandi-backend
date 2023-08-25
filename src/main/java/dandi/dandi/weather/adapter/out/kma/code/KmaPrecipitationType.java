package dandi.dandi.weather.adapter.out.kma.code;

import dandi.dandi.weather.adapter.out.kma.exception.KmaException;
import dandi.dandi.weather.domain.PrecipitationType;

import java.util.Arrays;

public enum KmaPrecipitationType {

    NONE(0, PrecipitationType.NONE),
    RAIN(1, PrecipitationType.RAIN),
    RAIN_SNOW(2, PrecipitationType.RAIN_SNOW),
    SNOW(3, PrecipitationType.SNOW),
    RAIN_SHOWER(4, PrecipitationType.RAIN),
    ;

    private final int code;
    private final PrecipitationType precipitationType;

    KmaPrecipitationType(int code, PrecipitationType precipitationType) {
        this.code = code;
        this.precipitationType = precipitationType;
    }

    public static KmaPrecipitationType from(int code) {
        return Arrays.stream(values())
                .filter(kmaPrecipitationType -> kmaPrecipitationType.code == code)
                .findAny()
                .orElseThrow(KmaException::precipitationTypeCode);
    }

    public PrecipitationType getPrecipitationType() {
        return precipitationType;
    }
}
