package dandi.dandi.clothes.application.service;

import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.in.ClothesUseCase;
import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Clothes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClothesService implements ClothesUseCase {

    private final ClothesPersistencePort clothesPersistencePort;

    public ClothesService(ClothesPersistencePort clothesPersistencePort) {
        this.clothesPersistencePort = clothesPersistencePort;
    }

    @Override
    @Transactional
    public void registerClothes(Long memberId, ClothesRegisterCommand clothesRegisterCommand) {
        Clothes clothes = clothesRegisterCommand.toClothes(memberId);
        clothesPersistencePort.save(clothes);
    }
}
