package dandi.dandi.clothes.adapter.persistence;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import dandi.dandi.common.PersistenceAdapterTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ClothesPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private ClothesPersistenceAdapter clothesPersistenceAdapter;

    @DisplayName("옷을 저장할 수 있다.")
    @Test
    void save() {
        Clothes clothes = Clothes.initial(MEMBER_ID, Category.TOP, List.of(Season.SPRING, Season.SUMMER),
                CLOTHES_IMAGE_URL);

        assertThatCode(() -> clothesPersistenceAdapter.save(clothes))
                .doesNotThrowAnyException();
    }

    @DisplayName("id로 옷을 찾을 수 있다.")
    @Test
    void findById() {
        Clothes clothes = Clothes.initial(MEMBER_ID, Category.TOP, List.of(Season.SPRING, Season.SUMMER),
                CLOTHES_IMAGE_URL);
        clothesPersistenceAdapter.save(clothes);

        Optional<Clothes> actual = clothesPersistenceAdapter.findById(1L);

        assertThat(actual).isPresent();
    }

    @DisplayName("id에 해당하는 옷을 삭제할 수 있다.")
    @Test
    void deleteById() {
        Clothes clothes = Clothes.initial(MEMBER_ID, Category.TOP, List.of(Season.SPRING, Season.SUMMER),
                CLOTHES_IMAGE_URL);
        clothesPersistenceAdapter.save(clothes);
        Optional<Clothes> foundBeforeDeletion = clothesPersistenceAdapter.findById(1L);

        clothesPersistenceAdapter.deleteById(1L);

        Optional<Clothes> foundAfterDeletion = clothesPersistenceAdapter.findById(1L);
        assertAll(
                () -> assertThat(foundBeforeDeletion).isPresent(),
                () -> assertThat(foundAfterDeletion).isEmpty()
        );
    }
}
