package dandi.dandi.clothes.application.service;

import dandi.dandi.clothes.application.port.in.CategorySeasonsResponse;
import dandi.dandi.clothes.application.port.in.CategorySeasonsResponses;
import dandi.dandi.clothes.application.port.in.ClothesDetailResponse;
import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.in.ClothesResponse;
import dandi.dandi.clothes.application.port.in.ClothesResponses;
import dandi.dandi.clothes.application.port.in.ClothesUseCase;
import dandi.dandi.clothes.application.port.out.persistence.CategorySeasonProjection;
import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Month;
import dandi.dandi.clothes.domain.Season;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClothesService implements ClothesUseCase {

    private static final int CLOTHES_IMAGE_URL_INDEX = 1;
    private static final String ALL = "ALL";
    private static final int TODAY_CLOTHES_COUNT_LIMIT = 5;

    private final ClothesPersistencePort clothesPersistencePort;
    private final ClothesImageService clothesImageService;
    private final String imageAccessUrl;

    public ClothesService(ClothesPersistencePort clothesPersistencePort, ClothesImageService clothesImageService,
                          @Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl) {
        this.clothesPersistencePort = clothesPersistencePort;
        this.clothesImageService = clothesImageService;
        this.imageAccessUrl = imageAccessUrl;
    }

    @Override
    @Transactional
    public void registerClothes(Long memberId, ClothesRegisterCommand clothesRegisterCommand) {
        String clothesImageUrl = removeImageAccessUrl(clothesRegisterCommand.getClothesImageUrl());
        Clothes clothes = Clothes.initial(memberId, clothesRegisterCommand.getCategory(),
                clothesRegisterCommand.getSeasons(), clothesImageUrl);
        clothesPersistencePort.save(clothes);
    }

    private String removeImageAccessUrl(String clothesImageUrl) {
        return clothesImageUrl.split(imageAccessUrl)[CLOTHES_IMAGE_URL_INDEX];
    }

    @Override
    @Transactional(readOnly = true)
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
        sortedCategoriesAndSeasons.put(ALL, allCategorySeasons);
        categoriesAndSeasons.forEach((key, value) -> sortedCategoriesAndSeasons.put(key.name(), value));
        return sortedCategoriesAndSeasons;
    }

    private CategorySeasonsResponses mapToCategoryResponses(Map<String, Set<Season>> categories) {
        List<CategorySeasonsResponse> categorySeasonsResponses = categories.entrySet()
                .stream()
                .map(entry -> new CategorySeasonsResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toUnmodifiableList());
        return new CategorySeasonsResponses(categorySeasonsResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public ClothesDetailResponse getSingleClothesDetails(Long memberId, Long clothesId) {
        Clothes clothes = clothesPersistencePort.findById(clothesId)
                .orElseThrow(NotFoundException::clothes);
        validateLookUpPermission(memberId, clothes);
        return new ClothesDetailResponse(clothes, imageAccessUrl);
    }

    private void validateLookUpPermission(Long memberId, Clothes clothes) {
        if (!clothes.isOwnedBy(memberId)) {
            throw ForbiddenException.clothesLookUp();
        }
    }

    @Override
    public ClothesResponses getClothes(Long memberId, String category, Set<String> seasons, Pageable pageable) {
        Slice<Clothes> clothesSearchResult = clothesPersistencePort.findByMemberIdAndCategoryAndSeasons(
                memberId, mapToCategory(category), mapToSeason(seasons), pageable);
        List<ClothesResponse> clothesResponses = clothesSearchResult.stream()
                .map(clothes -> new ClothesResponse(clothes.getId(), imageAccessUrl + clothes.getClothesImageUrl()))
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
    @Transactional
    public void deleteClothes(Long memberId, Long clothesId) {
        Clothes clothes = clothesPersistencePort.findById(clothesId)
                .orElseThrow(NotFoundException::clothes);
        validateOwner(clothes, memberId);
        clothesPersistencePort.deleteById(clothesId);
        clothesImageService.deleteClothesImage(clothes);
    }

    private void validateOwner(Clothes clothes, Long memberId) {
        if (!clothes.isOwnedBy(memberId)) {
            throw ForbiddenException.clothesDeletion();
        }
    }

    @Override
    public ClothesResponses getTodayClothes(Long memberId, LocalDate today, Pageable pageable) {
        Month month = Month.fromDate(today);
        Slice<Clothes> clothesSearchResult = clothesPersistencePort
                .findByMemberIdAndSeasons(memberId, month.getSeasons(), pageable);
        List<ClothesResponse> clothesResponses = clothesSearchResult.stream()
                .map(clothes -> new ClothesResponse(clothes.getId(), imageAccessUrl + clothes.getClothesImageUrl()))
                .collect(Collectors.toUnmodifiableList());
        return new ClothesResponses(clothesResponses, clothesSearchResult.isLast());
    }
}
