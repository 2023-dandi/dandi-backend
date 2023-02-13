package dandi.dandi.member.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LongitudeTest {

    @DisplayName("잘못된 경도 범위의 Longitude를 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(doubles = {-180.00001, 180.00001})
    void create_InvalidRange(double invalidLongitude) {
        assertThatThrownBy(() -> new Longitude(invalidLongitude))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경도 범위가 잘못되었습니다.");
    }
}