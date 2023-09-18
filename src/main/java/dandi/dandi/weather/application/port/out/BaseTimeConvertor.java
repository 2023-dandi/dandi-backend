package dandi.dandi.weather.application.port.out;

import java.time.LocalTime;

public interface BaseTimeConvertor {

    LocalTime convert(LocalTime localTime);
}
