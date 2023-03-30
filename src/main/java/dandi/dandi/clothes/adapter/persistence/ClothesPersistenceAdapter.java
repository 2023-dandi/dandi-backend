package dandi.dandi.clothes.adapter.persistence;

import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Clothes;
import java.util.Optional;
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
}
