package dandi.dandi.clothes.application.service;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_URL;
import static dandi.dandi.clothes.domain.Category.BAG;
import static dandi.dandi.clothes.domain.Category.BOTTOM;
import static dandi.dandi.clothes.domain.Category.TOP;
import static dandi.dandi.clothes.domain.Category.getAllCategories;
import static dandi.dandi.clothes.domain.Month.MAY;
import static dandi.dandi.clothes.domain.Season.FALL;
import static dandi.dandi.clothes.domain.Season.SPRING;
import static dandi.dandi.clothes.domain.Season.SUMMER;
import static dandi.dandi.common.constant.Constant.IMAGE_ACCESS_URL;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.utils.PaginationUtils.CREATED_AT_DESC_TEST_SIZE_PAGEABLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.DESC;

import dandi.dandi.clothes.application.port.in.CategorySeasonsResponse;
import dandi.dandi.clothes.application.port.in.CategorySeasonsResponses;
import dandi.dandi.clothes.application.port.in.ClothesDetailResponse;
import dandi.dandi.clothes.application.port.in.ClothesResponse;
import dandi.dandi.clothes.application.port.in.ClothesResponses;
import dandi.dandi.clothes.application.port.out.persistence.CategorySeasonProjection;
import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import dandi.dandi.common.exception.ForbiddenException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class ClothesQueryServiceAdapterTest {

    @Mock
    private ClothesPersistencePort clothesPersistencePort;
    @InjectMocks
    private ClothesQueryServiceAdapter clothesQueryServiceAdapter;

    @DisplayName("옷 세부 정보를 조회할 수 있다.")
    @Test
    void getSingleClothesDetails() {
        Long clothesId = 1L;
        when(clothesPersistencePort.findById(clothesId))
                .thenReturn(Optional.of(CLOTHES));

        ClothesDetailResponse clothesDetailResponse = clothesQueryServiceAdapter.getSingleClothesDetails(MEMBER_ID,
                clothesId);

        assertAll(
                () -> assertThat(clothesDetailResponse.getId()).isEqualTo(CLOTHES.getId()),
                () -> assertThat(clothesDetailResponse.getClothesImageUrl())
                        .isEqualTo(System.getProperty(IMAGE_ACCESS_URL) + CLOTHES.getClothesImageUrl())
        );
    }

    @DisplayName("다른 사람의 옷 세부 정보를 조회하려하면 예외를 발생시킨다.")
    @Test
    void getSingleClothesDetails_AnotherMemberClothes() {
        Long clothesId = 1L;
        Long anotherMember = 10L;
        when(clothesPersistencePort.findById(clothesId))
                .thenReturn(Optional.of(CLOTHES));

        assertThatThrownBy(() -> clothesQueryServiceAdapter.getSingleClothesDetails(anotherMember, clothesId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ForbiddenException.clothesLookUp().getMessage());
    }

    @DisplayName("카테고리와 카테고리에 따른 계절들을 조회할 수 있다.(빈 값)")
    @Test
    void getCategoriesAndSeasons_EmptyResult() {
        when(clothesPersistencePort.findDistinctCategoryAndSeason(MEMBER_ID))
                .thenReturn(List.of());

        CategorySeasonsResponses actual = clothesQueryServiceAdapter.getCategoriesAndSeasons(MEMBER_ID);

        AssertionsForInterfaceTypes.assertThat(actual.getCategories()).isEmpty();
    }

    @DisplayName("카테고리와 카테코리에 따른 계절들을 조회할 수 있다.")
    @Test
    void getCategoriesAndSeasons() {
        when(clothesPersistencePort.findDistinctCategoryAndSeason(MEMBER_ID))
                .thenReturn(List.of(new CategorySeasonProjectionTestImpl(TOP.name(), FALL.name()),
                        new CategorySeasonProjectionTestImpl(TOP.name(), SUMMER.name()),
                        new CategorySeasonProjectionTestImpl(BOTTOM.name(), SPRING.name())));

        CategorySeasonsResponses actual = clothesQueryServiceAdapter.getCategoriesAndSeasons(MEMBER_ID);

        List<CategorySeasonsResponse> categorySeasonsResponses = actual.getCategories();
        assertAll(
                () -> assertThat(categorySeasonsResponses.get(0).getCategory())
                        .isEqualTo("ALL"),
                () -> AssertionsForInterfaceTypes.assertThat(categorySeasonsResponses.get(0).getSeasons())
                        .isEqualTo(Set.of("SPRING", "SUMMER", "FALL")),
                () -> assertThat(categorySeasonsResponses.get(1).getCategory())
                        .isEqualTo("TOP"),
                () -> AssertionsForInterfaceTypes.assertThat(categorySeasonsResponses.get(1).getSeasons())
                        .isEqualTo(Set.of("SUMMER", "FALL")),
                () -> assertThat(categorySeasonsResponses.get(2).getCategory())
                        .isEqualTo("BOTTOM"),
                () -> AssertionsForInterfaceTypes.assertThat(categorySeasonsResponses.get(2).getSeasons())
                        .isEqualTo(Set.of("SPRING"))
        );
    }

    @DisplayName("카테고리, 계절 조건에 따른 내 옷을 조회할 수 있다.")
    @Test
    void getClothes() {
        Clothes clothes1 = new Clothes(1L, MEMBER_ID, TOP, List.of(SUMMER, FALL), CLOTHES_IMAGE_URL);
        Clothes clothes2 = new Clothes(1L, MEMBER_ID, TOP, List.of(SPRING, FALL), CLOTHES_IMAGE_URL);
        when(clothesPersistencePort.findByMemberIdAndCategoryAndSeasons(
                MEMBER_ID, Set.of(TOP), Set.of(SPRING, SUMMER), CREATED_AT_DESC_TEST_SIZE_PAGEABLE))
                .thenReturn(new SliceImpl<>(List.of(clothes2, clothes1), CREATED_AT_DESC_TEST_SIZE_PAGEABLE, false));

        ClothesResponses actual = clothesQueryServiceAdapter.getClothes(MEMBER_ID, TOP.name(),
                Set.of(SPRING.name(), SUMMER.name()), CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        ClothesResponse firstClothesResponse = actual.getClothes().get(0);
        assertAll(
                () -> assertThat(actual.isLastPage()).isTrue(),
                () -> assertThat(firstClothesResponse.getClothesImageUrl()).contains(CLOTHES_IMAGE_URL)
        );
    }

    @DisplayName("카테고리가 ALL이라면 모든 Category들을 검색할 수 있다.")
    @Test
    void getClothes_AllCategories() {
        Set<String> seasons = Set.of(SPRING.name(), SUMMER.name());
        Clothes clothes1 = new Clothes(1L, MEMBER_ID, TOP, List.of(SUMMER, FALL), CLOTHES_IMAGE_URL);
        Clothes clothes2 = new Clothes(1L, MEMBER_ID, TOP, List.of(SPRING, FALL), CLOTHES_IMAGE_URL);
        when(clothesPersistencePort.findByMemberIdAndCategoryAndSeasons(
                MEMBER_ID, getAllCategories(), Set.of(SPRING, SUMMER), CREATED_AT_DESC_TEST_SIZE_PAGEABLE))
                .thenReturn(new SliceImpl<>(List.of(clothes2, clothes1), CREATED_AT_DESC_TEST_SIZE_PAGEABLE, false));

        clothesQueryServiceAdapter.getClothes(MEMBER_ID, "ALL", seasons, CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        verify(clothesPersistencePort)
                .findByMemberIdAndCategoryAndSeasons(MEMBER_ID, getAllCategories(), Set.of(SPRING, SUMMER),
                        CREATED_AT_DESC_TEST_SIZE_PAGEABLE);
    }

    @DisplayName("날짜에 따른 사용자의 오늘의 옷을 최대 카테고리 조합으로 반환할 수 있다.")
    @Test
    void getTodayClothes() {
        Set<Season> seasons = MAY.getSeasons();
        Pageable pageable = PageRequest.of(0, 5, DESC, "createdAt");
        when(clothesPersistencePort.countDistinctCategoryByMemberIdAndSeasons(MEMBER_ID, seasons))
                .thenReturn(3);
        Clothes clothes1 = new Clothes(1L, MEMBER_ID, TOP, List.of(SUMMER, FALL), CLOTHES_IMAGE_URL);
        Clothes clothes2 = new Clothes(2L, MEMBER_ID, BOTTOM, List.of(SPRING, FALL), CLOTHES_IMAGE_URL);
        Clothes clothes3 = new Clothes(3L, MEMBER_ID, BAG, List.of(SPRING, FALL), CLOTHES_IMAGE_URL);
        Slice<Clothes> clothes = new SliceImpl<>(List.of(clothes1, clothes2, clothes3), pageable, false);
        when(clothesPersistencePort.findByMemberIdAndSeasonsWithCategoriesCount(MEMBER_ID, seasons, 3, pageable))
                .thenReturn(clothes);

        ClothesResponses clothesResponses =
                clothesQueryServiceAdapter.getTodayClothes(MEMBER_ID, LocalDate.of(2023, 5, 11), pageable);

        assertAll(
                () -> AssertionsForInterfaceTypes.assertThat(clothesResponses.getClothes()).hasSize(3),
                () -> assertThat(clothesResponses.isLastPage()).isTrue()
        );
    }

    static class CategorySeasonProjectionTestImpl implements CategorySeasonProjection {
        private final String category;
        private final String season;

        public CategorySeasonProjectionTestImpl(String category, String season) {
            this.category = category;
            this.season = season;
        }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public String getSeason() {
            return season;
        }
    }
}
