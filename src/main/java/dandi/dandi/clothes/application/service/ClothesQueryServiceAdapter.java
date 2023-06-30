package dandi.dandi.clothes.application.service;

import dandi.dandi.clothes.application.port.in.CategorySeasonsResponse;
import dandi.dandi.clothes.application.port.in.CategorySeasonsResponses;
import dandi.dandi.clothes.application.port.in.ClothesDetailResponse;
import dandi.dandi.clothes.application.port.in.ClothesQueryServicePort;
import dandi.dandi.clothes.application.port.in.ClothesResponse;
import dandi.dandi.clothes.application.port.in.ClothesResponses;
import dandi.dandi.clothes.application.port.out.persistence.CategorySeasonProjection;
import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Month;
import dandi.dandi.clothes.domain.Season;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.image.aspect.ImageUrlInclusion;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClothesQueryServiceAdapter implements ClothesQueryServicePort {

    private static final String ALL = "ALL";

    private final ClothesPersistencePort clothesPersistencePort;

    public ClothesQueryServiceAdapter(ClothesPersistencePort clothesPersistencePort) {
        this.clothesPersistencePort = clothesPersistencePort;
    }

    @Override
    public CategorySeasonsResponses getCategoriesAndSeasons(Long memberId) {
        Map<Category, EnumSet<Season>> categoriesAndSeasons = new EnumMap<>(Category.class);
        for (CategorySeasonProjection categorySeason :
                clothesPersistencePort.findDistinctCategoryAndSeason(memberId)) {
            EnumSet<Season> seasons = categoriesAndSeasons.computeIfAbsent(
                    Category.from(categorySeason.getCategory()), ignored -> EnumSet.noneOf(Season.class));
            seasons.add(Season.from(categorySeason.getSeason()));
        }
        return mapToCategoryResponses(addAllCategorySeasons(categoriesAndSeasons));
    }

    private Map<String, Set<Season>> addAllCategorySeasons(Map<Category, EnumSet<Season>> categoriesAndSeasons) {
        Map<String, Set<Season>> sortedCategoriesAndSeasons = new HashMap<>();
        Set<Season> allCategorySeasons = categoriesAndSeasons.values()
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toUnmodifiableSet());
        addAllCategoryIfNotEmpty(sortedCategoriesAndSeasons, allCategorySeasons);
        categoriesAndSeasons.forEach((key, value) -> sortedCategoriesAndSeasons.put(key.name(), value));
        return sortedCategoriesAndSeasons;
    }

    private void addAllCategoryIfNotEmpty(Map<String, Set<Season>> sortedCategoriesAndSeasons,
                                          Set<Season> allCategorySeasons) {
        if (!allCategorySeasons.isEmpty()) {
            sortedCategoriesAndSeasons.put(ALL, allCategorySeasons);
        }
    }

    private CategorySeasonsResponses mapToCategoryResponses(Map<String, Set<Season>> categories) {
        List<CategorySeasonsResponse> categorySeasonsResponses = categories.entrySet()
                .stream()
                .map(entry -> new CategorySeasonsResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toUnmodifiableList());
        return new CategorySeasonsResponses(categorySeasonsResponses);
    }

    @Override
    @ImageUrlInclusion
    public ClothesDetailResponse getSingleClothesDetails(Long memberId, Long clothesId) {
        Clothes clothes = clothesPersistencePort.findById(clothesId)
                .orElseThrow(NotFoundException::clothes);
        validateLookUpPermission(memberId, clothes);
        return new ClothesDetailResponse(clothes);
    }

    private void validateLookUpPermission(Long memberId, Clothes clothes) {
        if (!clothes.isOwnedBy(memberId)) {
            throw ForbiddenException.clothesLookUp();
        }
    }

    @Override
    @ImageUrlInclusion
    public ClothesResponses getClothes(Long memberId, String category, Set<String> seasons, Pageable pageable) {
        Slice<Clothes> clothesSearchResult = clothesPersistencePort.findByMemberIdAndCategoryAndSeasons(
                memberId, mapToCategory(category), mapToSeason(seasons), pageable);
        List<ClothesResponse> clothesResponses = clothesSearchResult.stream()
                .map(ClothesResponse::new)
                .collect(Collectors.toUnmodifiableList());
        return new ClothesResponses(clothesResponses, clothesSearchResult.isLast());
    }

    private Set<Category> mapToCategory(String category) {
        if (category.equals(ALL)) {
            return Category.getAllCategories();
        }
        return Set.of(Category.from(category));
    }

    private Set<Season> mapToSeason(Set<String> seasons) {
        return seasons.stream()
                .map(Season::from)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @ImageUrlInclusion
    public ClothesResponses getTodayClothes(Long memberId, LocalDate today, Pageable pageable) {
        Month month = Month.fromDate(today);
        Set<Season> seasons = month.getSeasons();
        int categoriesCount = clothesPersistencePort.countDistinctCategoryByMemberIdAndSeasons(memberId, seasons);
        int searchCategoryCount = Math.min(categoriesCount, pageable.getPageSize());
        Slice<Clothes> clothesSearchResult = clothesPersistencePort
                .findByMemberIdAndSeasonsWithCategoriesCount(memberId, seasons, searchCategoryCount, pageable);
        List<ClothesResponse> clothesResponses = clothesSearchResult.stream()
                .map(ClothesResponse::new)
                .collect(Collectors.toUnmodifiableList());
        return new ClothesResponses(clothesResponses, clothesSearchResult.isLast());
    }
}
