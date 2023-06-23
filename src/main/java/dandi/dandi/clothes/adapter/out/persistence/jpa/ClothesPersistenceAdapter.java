package dandi.dandi.clothes.adapter.out.persistence.jpa;

import dandi.dandi.clothes.application.port.out.persistence.CategorySeasonProjection;
import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
public class ClothesPersistenceAdapter implements ClothesPersistencePort {

    private final ClothesRepository clothesRepository;

    public ClothesPersistenceAdapter(ClothesRepository clothesRepository) {
        this.clothesRepository = clothesRepository;
    }

    @Override
    public void save(Clothes clothes) {
        ClothesJpaEntity clothesJpaEntity = ClothesJpaEntity.fromClothes(clothes);
        clothesRepository.save(clothesJpaEntity);
    }

    @Override
    public Optional<Clothes> findById(Long id) {
        return clothesRepository.findById(id)
                .map(ClothesJpaEntity::toClothes);
    }

    @Override
    public Slice<Clothes> findByMemberIdAndCategoryAndSeasons(Long memberId, Set<Category> categories,
                                                              Set<Season> seasons, Pageable pageable) {
        Slice<ClothesJpaEntity> clothesJpaEntities =
                clothesRepository.findByMemberIdAndCategoryAndSeasons(memberId, categories, seasons, pageable);
        List<Clothes> clothes = clothesJpaEntities.stream()
                .map(ClothesJpaEntity::toClothes)
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(clothes, pageable, clothesJpaEntities.hasNext());
    }

    @Override
    public List<CategorySeasonProjection> findDistinctCategoryAndSeason(Long memberId) {
        return clothesRepository.findAllByCategoryDistinct(memberId);
    }

    @Override
    public void deleteById(Long clothesId) {
        clothesRepository.deleteById(clothesId);
    }

    @Override
    public int countDistinctCategoryByMemberIdAndSeasons(Long memberId, Set<Season> seasons) {
        return clothesRepository.countDistinctCategoryByMemberIdAndSeasons(memberId, seasons);
    }

    @Override
    public Slice<Clothes> findByMemberIdAndSeasonsWithCategoriesCount(Long memberId, Set<Season> seasons,
                                                                      int categoriesCount, Pageable pageable) {
        Slice<ClothesJpaEntity> clothesJpaEntities =
                clothesRepository.findByMemberIdAndSeasons(memberId, categoriesCount, seasons, pageable);
        List<Clothes> clothes = clothesJpaEntities
                .stream()
                .map(ClothesJpaEntity::toClothes)
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(clothes, pageable, clothesJpaEntities.hasNext());
    }
}

