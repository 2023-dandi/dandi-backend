package dandi.dandi.clothes.adapter.persistence;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import dandi.dandi.common.PersistenceAdapterTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ClothesPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private ClothesPersistenceAdapter clothesPersistenceAdapter;

    @DisplayName("옷을 저장할 수 있다.")
    @Test
    void save() {
        Clothes clothes = Clothes.initial(Category.TOP, List.of(Season.SPRING, Season.SUMMER), CLOTHES_IMAGE_URL);

        assertThatCode(() -> clothesPersistenceAdapter.save(clothes, 1L))
                .doesNotThrowAnyException();
    }
}
