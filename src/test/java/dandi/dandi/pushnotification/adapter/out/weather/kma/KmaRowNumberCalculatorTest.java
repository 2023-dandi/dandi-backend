package dandi.dandi.pushnotification.adapter.out.weather.kma;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KmaRowNumberCalculatorTest {

    private final KmaRowNumberCalculator kmaRowNumberCalculator = new KmaRowNumberCalculator();

    @DisplayName("KMA 날씨 데이터의 요청 row number를 계산한다.")
    @ParameterizedTest
    @CsvSource({"22, 350", "3, 280"})
    void calculateNumOfRows(int hour, int expected) {
        LocalTime time = LocalTime.of(hour, 0);

        int numOfRows = kmaRowNumberCalculator.calculateNumOfRows(time);

        assertThat(numOfRows).isEqualTo(expected);
    }
}
