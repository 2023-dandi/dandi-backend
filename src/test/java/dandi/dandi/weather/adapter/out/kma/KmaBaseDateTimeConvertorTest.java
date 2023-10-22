package dandi.dandi.weather.adapter.out.kma;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class KmaBaseDateTimeConvertorTest {

    private final KmaBaseDateTimeConvertor kmaBaseTimeConvertor = new KmaBaseDateTimeConvertor();

    @ParameterizedTest
    @DisplayName("현재 시각을 BaseTime으로 변환한다.")
    @CsvSource({"0, 23, 28", "1, 23, 28", "2, 2, 29", "3, 2, 29", "4, 2, 29", "5, 5, 29", "6, 5, 29", "7, 5, 29",
            "8, 8, 29", "9, 8, 29", "10, 8, 29", "11, 11, 29", "12, 11, 29", "13, 11, 29", "14, 14, 29", "15, 14, 29",
            "16, 14, 29", "17, 17, 29", "18, 17, 29", "19, 17, 29", "20, 20, 29", "21, 20, 29", "22, 20, 29", "23, 23, 29"})
    void convert(int hour, int expectedHour, int expectedDay) {
        LocalDateTime actual = kmaBaseTimeConvertor.convert(LocalDateTime.of(2023, 10, 29, hour, 15, 0));

        assertAll(
                () -> assertThat(actual.getHour()).isEqualTo(expectedHour),
                () -> assertThat(actual.getDayOfMonth()).isEqualTo(expectedDay)
        );
    }
}
