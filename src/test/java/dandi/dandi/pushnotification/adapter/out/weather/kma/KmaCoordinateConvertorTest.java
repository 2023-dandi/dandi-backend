package dandi.dandi.pushnotification.adapter.out.weather.kma;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KmaCoordinateConvertorTest {

    private final KmaCoordinateConvertor kmaCoordinateConvertor = new KmaCoordinateConvertor();

    @DisplayName("위도, 경도를 기상청 nx, ny 값으로 변환한다.")
    @ParameterizedTest
    @CsvSource({"37.56356944444444, 126.98000833333333, 60, 127", "35.82795555555556, 128.53049722222224, 88, 90"})
    void convert(double latitude, double longitude, int expectedNx, int expectedNy) {
        Coordinate coordinate = kmaCoordinateConvertor.convert(latitude, longitude);

        assertAll(
                () -> assertThat(coordinate.getNx()).isEqualTo(expectedNx),
                () -> assertThat(coordinate.getNy()).isEqualTo(expectedNy)
        );
    }
}
