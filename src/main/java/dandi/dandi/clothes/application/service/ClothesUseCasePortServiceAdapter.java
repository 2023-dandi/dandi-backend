package dandi.dandi.clothes.application.service;

import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.in.ClothesUseCasePort;
import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClothesUseCasePortServiceAdapter implements ClothesUseCasePort {

    private static final int CLOTHES_IMAGE_URL_INDEX = 1;

    private final ClothesPersistencePort clothesPersistencePort;
    private final ClothesImageService clothesImageService;
    private final String imageAccessUrl;

    public ClothesUseCasePortServiceAdapter(ClothesPersistencePort clothesPersistencePort,
                                            ClothesImageService clothesImageService,
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
}
