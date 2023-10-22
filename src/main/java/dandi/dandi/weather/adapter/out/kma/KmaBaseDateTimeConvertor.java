package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.application.port.out.BaseDateTimeConvertor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class KmaBaseDateTimeConvertor implements BaseDateTimeConvertor {

    private static final List<LocalTime> BASE_TIMES;

    static {
        List<LocalTime> baseTimes = new ArrayList<>();
        for (int i = 23; i >= 1; i -= 3) {
            baseTimes.add(LocalTime.of(i, 0));
        }
        BASE_TIMES = baseTimes;
    }

    public LocalDateTime convert(LocalDateTime localDateTime) {
        LocalDate today = localDateTime.toLocalDate();
        if (localDateTime.getHour() == 0 || localDateTime.getHour() == 1) {
            LocalDate yesterday = today.minusDays(1);
            return LocalDateTime.of(yesterday, getBaseTimeOnMidnight());
        }
        int hour = localDateTime.getHour();
        LocalTime baseTime = BASE_TIMES.stream()
                .filter(time -> hour >= time.getHour())
                .findFirst()
                .orElseThrow();
        return LocalDateTime.of(today, baseTime);
    }

    private LocalTime getBaseTimeOnMidnight() {
        return BASE_TIMES.get(0);
    }
}
