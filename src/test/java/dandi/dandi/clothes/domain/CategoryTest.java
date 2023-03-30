package dandi.dandi.clothes.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CategoryTest {

    @DisplayName("옷 카테고리 값으로 Category를 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"TOP", "BOTTOM", "OUTER", "ONE_PIECE", "SHOES", "CAP", "ETC"})
    void create(String categoryValue) {
        assertThatCode(() -> Category.from(categoryValue))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 옷 카테고리 값으로 Category를 생성하려 하면 예외를 발생시킨다.")
    @Test
    void create_InvalidCategoryValue() {
        String invalidCategoryValue = "TOPPP";

        assertThatThrownBy(() -> Category.from(invalidCategoryValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 옷 카테고리입니다.");
    }
}
