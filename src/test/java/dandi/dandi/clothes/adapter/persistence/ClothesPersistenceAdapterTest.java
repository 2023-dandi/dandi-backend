package dandi.dandi.clothes.adapter.persistence;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES_CATEGORY;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_URL;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_SEASONS;
import static dandi.dandi.clothes.domain.Category.TOP;
import static dandi.dandi.clothes.domain.Season.SPRING;
import static dandi.dandi.clothes.domain.Season.SUMMER;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Direction.DESC;

import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.common.PersistenceAdapterTest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

class ClothesPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private ClothesPersistenceAdapter clothesPersistenceAdapter;

    @DisplayName("옷을 저장할 수 있다.")
    @Test
    void save() {
        Clothes clothes = Clothes.initial(MEMBER_ID, CLOTHES_CATEGORY, CLOTHES_SEASONS, CLOTHES_IMAGE_URL);

        assertThatCode(() -> clothesPersistenceAdapter.save(clothes))
                .doesNotThrowAnyException();
    }

    @DisplayName("id로 옷을 찾을 수 있다.")
    @Test
    void findById() {
        Clothes clothes = Clothes.initial(MEMBER_ID, CLOTHES_CATEGORY, CLOTHES_SEASONS, CLOTHES_IMAGE_URL);
        clothesPersistenceAdapter.save(clothes);

        Optional<Clothes> actual = clothesPersistenceAdapter.findById(1L);

        assertThat(actual).isPresent();
    }

    @DisplayName("id에 해당하는 옷을 삭제할 수 있다.")
    @Test
    void deleteById() {
        Clothes clothes = Clothes.initial(MEMBER_ID, CLOTHES_CATEGORY, CLOTHES_SEASONS, CLOTHES_IMAGE_URL);
        clothesPersistenceAdapter.save(clothes);
        Optional<Clothes> foundBeforeDeletion = clothesPersistenceAdapter.findById(1L);

        clothesPersistenceAdapter.deleteById(1L);

        Optional<Clothes> foundAfterDeletion = clothesPersistenceAdapter.findById(1L);
        assertAll(
                () -> assertThat(foundBeforeDeletion).isPresent(),
                () -> assertThat(foundAfterDeletion).isEmpty()
        );
    }

    @DisplayName("카테고리, 온도에 따른 특정 회원의 옷을 찾을 수 있다.")
    @Test
    void findByMemberIdAndCategoryAndSeasons() {
        Clothes clothes1 = Clothes.initial(MEMBER_ID, "TOP", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL);
        Clothes clothes2 = Clothes.initial(MEMBER_ID, "TOP", List.of("FALL", "SUMMER"), CLOTHES_IMAGE_URL);
        Clothes clothes3 = Clothes.initial(MEMBER_ID, "TOP", List.of("FALL", "WINTER"), CLOTHES_IMAGE_URL);
        Clothes clothes4 = Clothes.initial(MEMBER_ID, "BOTTOM", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL);
        clothesPersistenceAdapter.save(clothes1);
        clothesPersistenceAdapter.save(clothes2);
        clothesPersistenceAdapter.save(clothes3);
        clothesPersistenceAdapter.save(clothes4);
        PageRequest pageable = PageRequest.of(0, 10, DESC, "createdAt");

        Slice<Clothes> actual = clothesPersistenceAdapter.findByMemberIdAndCategoryAndSeasons(
                MEMBER_ID, TOP, Set.of(SPRING, SUMMER), pageable);

        assertThat(actual).hasSize(2);
    }
}
