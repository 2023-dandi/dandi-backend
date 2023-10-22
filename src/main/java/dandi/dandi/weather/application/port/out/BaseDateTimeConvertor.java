package dandi.dandi.weather.application.port.out;

import java.time.LocalDateTime;

public interface BaseDateTimeConvertor {

    LocalDateTime convert(LocalDateTime localDateTime);
}
