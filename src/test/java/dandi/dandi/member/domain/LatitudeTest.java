package dandi.dandi.member.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LatitudeTest {

    @DisplayName("잘못된 위도 범위의 Latitude를 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(doubles = {-90.00001, 90.00001})
    void create_InvalidRange(double invalidLatitude) {
        assertThatThrownBy(() -> new Latitude(invalidLatitude))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("위도 범위가 잘못되었습니다.");
    }
}
