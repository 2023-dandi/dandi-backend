package dandi.dandi.clothes.application.service;

import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.in.ClothesResponse;
import dandi.dandi.clothes.application.port.in.ClothesResponses;
import dandi.dandi.clothes.application.port.in.ClothesUseCase;
import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import java.util.List;
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
    public ClothesResponses getClothes(Long memberId, String category, Set<String> seasons, Pageable pageable) {
        Slice<Clothes> clothesSearchResult = clothesPersistencePort
                .findByMemberIdAndCategoryAndSeasons(memberId, Category.from(category), mapToSeason(seasons), pageable);
        List<ClothesResponse> clothesResponses = clothesSearchResult.stream()
                .map(clothes -> new ClothesResponse(clothes.getId(), imageAccessUrl + clothes.getClothesImageUrl()))
                .collect(Collectors.toUnmodifiableList());
        return new ClothesResponses(clothesResponses, clothesSearchResult.isLast());
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
}
