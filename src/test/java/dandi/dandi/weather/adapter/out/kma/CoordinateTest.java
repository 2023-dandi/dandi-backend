package dandi.dandi.weather.adapter.out.kma;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CoordinateTest {

    @DisplayName("다른 좌표와 range를 받아 nx, ny의 차이가 range 보다 작거나 같은지 반환한다.")
    @ParameterizedTest
    @CsvSource({"0, 0, true", "-5, 5, true", "5, 6, false"})
    void isInRange(int nx, int ny, boolean expected) {
        Coordinate coordinate = new Coordinate(0, 0);
        Coordinate another = new Coordinate(nx, ny);
        int range = 5;

        boolean actual = coordinate.isInRange(another, range);

        assertThat(actual).isEqualTo(expected);
    }
}
