package dandi.dandi.clothes.adapter.persistence;

import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Clothes;
import org.springframework.stereotype.Component;

@Component
public class ClothesPersistenceAdapter implements ClothesPersistencePort {

    private final ClothesRepository clothesRepository;

    public ClothesPersistenceAdapter(ClothesRepository clothesRepository) {
        this.clothesRepository = clothesRepository;
    }

    @Override
    public void save(Clothes clothes, Long memberId) {
        ClothesJpaEntity clothesJpaEntity = ClothesJpaEntity.fromClothesAndMemberId(clothes, memberId);
        clothesRepository.save(clothesJpaEntity);
    }
}
