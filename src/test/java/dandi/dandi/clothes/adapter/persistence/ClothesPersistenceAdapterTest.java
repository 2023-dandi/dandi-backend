package dandi.dandi.clothes.adapter.persistence;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES_CATEGORY;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_URL;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_SEASONS;
import static dandi.dandi.clothes.domain.Category.BOTTOM;
import static dandi.dandi.clothes.domain.Category.TOP;
import static dandi.dandi.clothes.domain.Season.FALL;
import static dandi.dandi.clothes.domain.Season.SPRING;
import static dandi.dandi.clothes.domain.Season.SUMMER;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.utils.PaginationUtils.CREATED_AT_DESC_TEST_SIZE_PAGEABLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Direction.DESC;

import dandi.dandi.clothes.application.port.out.persistence.CategorySeasonProjection;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.common.PersistenceAdapterTest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @DisplayName("회원의 옷을 카테고리와 계절 기준으로 중복을 제거해서 찾을 수 있다.")
    @Test
    void findDistinctByCategoryAndSeason() {
        saveClothes(List.of(
                Clothes.initial(MEMBER_ID, "TOP", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "TOP", List.of("FALL", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "TOP", List.of("FALL", "WINTER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "BOTTOM", List.of("FALL", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "BAG", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL)
        ));

        List<CategorySeasonProjection> actual = clothesPersistenceAdapter.findDistinctCategoryAndSeason(MEMBER_ID);

        int categoriesDistinctCount = (int) actual.stream()
                .map(CategorySeasonProjection::getCategory)
                .distinct()
                .count();
        assertAll(
                () -> assertThat(actual).hasSize(8),
                () -> assertThat(categoriesDistinctCount).isEqualTo(3)
        );
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
        saveClothes(List.of(
                Clothes.initial(MEMBER_ID, "TOP", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "TOP", List.of("FALL", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "TOP", List.of("FALL", "WINTER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "BOTTOM", List.of("FALL", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "BAG", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL)
        ));

        Slice<Clothes> actual = clothesPersistenceAdapter.findByMemberIdAndCategoryAndSeasons(
                MEMBER_ID, Set.of(TOP, BOTTOM), Set.of(SPRING, SUMMER), CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        assertThat(actual).hasSize(3);
    }

    @DisplayName("계절에 해당하는 옷의 category 개수를 알 수 있다.")
    @Test
    void countDistinctCategoryByMemberIdAndSeasons() {
        saveClothes(List.of(
                Clothes.initial(MEMBER_ID, "TOP", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "TOP", List.of("FALL", "WINTER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "BOTTOM", List.of("FALL", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "BAG", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL)
        ));

        int actual = clothesPersistenceAdapter
                .countDistinctCategoryByMemberIdAndSeasons(MEMBER_ID, Set.of(SUMMER, FALL));

        assertThat(actual).isEqualTo(3);
    }

    @DisplayName("계절에 해당하는 옷들을 2개 이상의 카테고리 조합으로 찾을 수 있다.")
    @Test
    void findByMemberIdWithCategoryDistinctCountNotOne() {
        saveClothes(List.of(
                Clothes.initial(MEMBER_ID, "TOP", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "TOP", List.of("SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "TOP", List.of("FALL", "WINTER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "BOTTOM", List.of("FALL", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(MEMBER_ID, "BAG", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(2L, "TOP", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(2L, "TOP", List.of("SUMMER"), CLOTHES_IMAGE_URL),
                Clothes.initial(2L, "TOP", List.of("FALL", "WINTER"), CLOTHES_IMAGE_URL),
                Clothes.initial(2L, "BOTTOM", List.of("FALL", "SUMMER"), CLOTHES_IMAGE_URL)
        ));
        Pageable pageable = PageRequest.of(0, 5, DESC, "createdAt");
        int categoriesDistinctCount = 3;

        Slice<Clothes> clothes = clothesPersistenceAdapter
                .findByMemberIdAndSeasonsWithCategoriesCount(MEMBER_ID, Set.of(SPRING, SUMMER), 3, pageable);

        int actualCategoriesDistinctCount = (int) clothes.stream()
                .map(Clothes::getCategory)
                .distinct()
                .count();
        assertAll(
                () -> assertThat(clothes).hasSize(4),
                () -> assertThat(actualCategoriesDistinctCount).isEqualTo(categoriesDistinctCount)
        );
    }

    private void saveClothes(List<Clothes> clothes) {
        clothes.forEach(it -> clothesPersistenceAdapter.save(it));
    }
}

