package dandi.dandi.pushnotification.adapter.out.weather.kma;

import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class KmaRowNumberCalculator {

    private static final int ROW_NUMBERS_BY_HOUR = 14;
    private static final LocalTime FORECAST_DAY_STANDARD_TIME = LocalTime.of(15, 0);

    public int calculateNumOfRows(LocalTime nowTime) {
        int firstForecastHour = nowTime.getHour() + 1;
        int totalForecastHoursCount = 24 - firstForecastHour;
        int todayForecastRowNums = totalForecastHoursCount * ROW_NUMBERS_BY_HOUR;
        if (isTomorrowForecast(nowTime)) {
            int tomorrowForecastRowNums = 24 * ROW_NUMBERS_BY_HOUR;
            return tomorrowForecastRowNums + todayForecastRowNums;
        }
        return todayForecastRowNums;
    }

    private boolean isTomorrowForecast(LocalTime time) {
        return time.isAfter(FORECAST_DAY_STANDARD_TIME);
    }
}
