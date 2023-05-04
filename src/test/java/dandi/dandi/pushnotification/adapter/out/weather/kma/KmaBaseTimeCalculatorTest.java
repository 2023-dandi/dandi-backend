package dandi.dandi.pushnotification.adapter.out.weather.kma;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KmaBaseTimeCalculatorTest {

    private final KmaBaseTimeCalculator kmaBaseTimeCalculator = new KmaBaseTimeCalculator();

    @DisplayName("basetime(가장 최근에 업데이트된 예보 시간)을 계산한다.")
    @ParameterizedTest
    @CsvSource({"5, 11, 4, 5", "5, 10, 4, 2", "2, 11, 4, 2", "2, 10, 3, 23"})
    void calculateBaseTime(int nowHour, int nowMinute, int expectedBaseDay, int expectedBaseHour) {
        LocalDateTime now = LocalDateTime.of(2023, 5, 4, nowHour, nowMinute);

        LocalDateTime actual = kmaBaseTimeCalculator.calculateBaseDateTime(now);

        assertAll(
                () -> assertThat(actual.getHour()).isEqualTo(expectedBaseHour),
                () -> assertThat(actual.getDayOfMonth()).isEqualTo(expectedBaseDay)
        );
    }
}
