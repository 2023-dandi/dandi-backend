package dandi.dandi.clothes.domain;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_URL;
import static dandi.dandi.clothes.domain.Category.TOP;
import static dandi.dandi.clothes.domain.Season.SUMMER;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ClothesTest {

    @DisplayName("id에 해당하는 회원의 옷인지 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void isOwnedBy(Long memberId, boolean expected) {
        Clothes clothes = Clothes.initial(1L, TOP, List.of(SUMMER), CLOTHES_IMAGE_URL);

        boolean actual = clothes.isOwnedBy(memberId);

        assertThat(actual).isEqualTo(expected);
    }
}
