package dandi.dandi.clothes.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MonthTest {

    @DisplayName("날짜에 해당하는 Month 객체를 생성할 수 있다.")
    @CsvSource({"1, JANUARY", "2, FEBRUARY", "3, MARCH", "4, APRIL", "5, MAY", "6, JUNE",
            "7, JULY", "8, AUGUST", "9, SEPTEMBER", "10, OCTOBER", "11, NOVEMBER", "12, DECEMBER"})
    @ParameterizedTest
    void fromDate(int month, Month expected) {
        LocalDate localDate = LocalDate.of(2022, java.time.Month.of(month), 1);

        Month actual = Month.fromDate(localDate);

        assertThat(actual).isEqualTo(expected);
    }
}
