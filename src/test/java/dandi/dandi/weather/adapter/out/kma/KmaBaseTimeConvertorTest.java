package dandi.dandi.weather.adapter.out.kma;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class KmaBaseTimeConvertorTest {

    private final KmaBaseTimeConvertor kmaBaseTimeConvertor = new KmaBaseTimeConvertor();

    @ParameterizedTest
    @DisplayName("현재 시각을 BaseTime으로 변환한다.")
    @CsvSource({"0, 23", "1, 23", "2, 2", "3, 2", "4, 2", "5, 5", "6, 5", "7, 5",
    "8, 8", "9, 8", "10, 8", "11, 11", "12, 11", "13, 11", "14, 14", "15, 14", "16, 14",
    "17, 17", "18, 17", "19, 17", "20, 20", "21, 20", "22, 20", "23, 23"})
    void convert(int hour, int expected) {
        LocalTime actual = kmaBaseTimeConvertor.convert(LocalTime.of(hour, 15));

        assertThat(actual.getHour()).isEqualTo(expected);
    }
}