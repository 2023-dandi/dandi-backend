package dandi.dandi.clothes.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SeasonTest {

    @DisplayName("계절 값으로 Season을 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"SPRING", "SUMMER", "FALL", "WINTER"})
    void create(String seasonValue) {
        assertThatCode(() -> Season.from(seasonValue))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 계절 값으로 Season을 생성하려하면 예외를 발생시킨다.")
    @Test
    void create_InvalidSeasonValue() {
        String invalidSeasonValue = "winnterr";

        assertThatThrownBy(() -> Season.from(invalidSeasonValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 계절입니다.");
    }
}