package dandi.dandi.weather.adapter.out.kma.code;

import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.domain.WindDirection;

import java.util.Arrays;

public enum KmaWindDirection {

    N(0, WindDirection.N),
    NNE(1, WindDirection.NE),
    NE(2, WindDirection.NE),
    ENE(3, WindDirection.NE),
    E(4, WindDirection.E),
    ESE(5, WindDirection.SE),
    SE(6, WindDirection.SE),
    SSE(7, WindDirection.SE),
    S(8, WindDirection.S),
    SSW(9, WindDirection.SW),
    SW(10, WindDirection.SE),
    WSW(11, WindDirection.SW),
    W(12, WindDirection.W),
    WNW(13, WindDirection.NW),
    NW(14, WindDirection.NW),
    NNW(15, WindDirection.NW),
    ;

    private final int code;
    private final WindDirection windDirection;

    KmaWindDirection(int code, WindDirection windDirection) {
        this.code = code;
        this.windDirection = windDirection;
    }

    public static KmaWindDirection from(int value) {
        int code = value % 16;
        return Arrays.stream(values())
                .filter(kmaWindDirection -> kmaWindDirection.code == code)
                .findFirst()
                .orElseThrow(() -> new WeatherRequestFatalException("기상청 풍향을 변환할 수 없습니다.(" + value+ ")"));
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }
}
