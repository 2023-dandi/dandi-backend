package dandi.dandi.clothes.adapter.persistence;

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
    public Slice<Clothes> findByMemberIdAndCategoryAndSeasons(Long memberId, Category category,
                                                              Set<Season> seasons, Pageable pageable) {
        Slice<ClothesJpaEntity> clothesJpaEntities =
                clothesRepository.findByMemberIdAndCategoryAndSeasons(memberId, category, seasons, pageable);
        List<Clothes> clothes = clothesJpaEntities.stream()
                .map(ClothesJpaEntity::toClothes)
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(clothes, pageable, clothesJpaEntities.hasNext());
    }

    @Override
    public void deleteById(Long clothesId) {
        clothesRepository.deleteById(clothesId);
    }
}
