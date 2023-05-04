package dandi.dandi.pushnotification.adapter.out.weather.kma;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class KmaBaseTimeCalculator {

    private static final LocalTime FIRST_API_AVAILABLE_TIME_OF_DAY = LocalTime.of(2, 11);
    private static final Map<LocalTime, LocalTime> BASE_TIME_BY_API_AVAILABLE_TIME = new LinkedHashMap<>();

    static {
        for (int hour = 23; hour >= 1; hour -= 3) {
            BASE_TIME_BY_API_AVAILABLE_TIME.put(LocalTime.of(hour, 10), LocalTime.of(hour, 0));
        }
    }

    public LocalDateTime calculateBaseDateTime(LocalDateTime now) {
        if (hasNoTodayForecast(now)) {
            return generateYesterdayLastBaseDateTime(now);
        }
        LocalTime baseTime = BASE_TIME_BY_API_AVAILABLE_TIME.keySet()
                .stream()
                .filter(apiAvailableTime -> now.toLocalTime().isAfter(apiAvailableTime))
                .findFirst()
                .map(BASE_TIME_BY_API_AVAILABLE_TIME::get)
                .orElse(LocalTime.of(2, 0));
        return LocalDateTime.of(now.toLocalDate(), baseTime);
    }

    private boolean hasNoTodayForecast(LocalDateTime now) {
        return now.toLocalTime()
                .isBefore(FIRST_API_AVAILABLE_TIME_OF_DAY);
    }

    private LocalDateTime generateYesterdayLastBaseDateTime(LocalDateTime now) {
        return LocalDateTime.of(now.getYear(), now.getMonth(),
                now.getDayOfMonth() - 1,
                23, 0);
    }
}
