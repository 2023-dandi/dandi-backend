package dandi.dandi.member.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LocationUpdateCommandTest {

    private static final String INVALID_LOCATION_EXCEPTION_MESSAGE = "위도, 경도 값이 잘못되었습니다";

    @DisplayName("위도 -90.0 ~ 90.0, 경도 -180.0 ~ 180.0 범위가 아닌 값으로 LocationUpdateCommand를 생성하려하면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"-90.1, 0", "90.1, 0", "0, -180.1", "0, 180.1", "90.1, -180.1"})
    void create_Exception(Double latitude, Double longitude) {
        assertThatThrownBy(() -> new LocationUpdateCommand(latitude, longitude))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_LOCATION_EXCEPTION_MESSAGE);
    }
}