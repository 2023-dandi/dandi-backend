package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.application.port.out.BaseTimeConvertor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class KmaBaseTimeConvertor implements BaseTimeConvertor {

    private static final List<LocalTime> BASE_TIMES;

    static {
        List<LocalTime> baseTimes = new ArrayList<>();
        for (int i = 23; i >= 1; i -= 3) {
            baseTimes.add(LocalTime.of(i, 0));
        }
        BASE_TIMES = baseTimes;
    }

    public LocalTime convert(LocalTime localTime) {
        if (localTime.getHour() == 0 || localTime.getHour() == 1) {
            return getBaseTimeOnMidnight();
        }
        int hour = localTime.getHour();
        return BASE_TIMES.stream()
                .filter(baseTime -> hour >= baseTime.getHour())
                .findFirst()
                .orElseThrow();
    }

    private LocalTime getBaseTimeOnMidnight() {
        return BASE_TIMES.get(0);
    }
}
