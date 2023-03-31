package dandi.dandi.clothes.application.port.in;


import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface ClothesUseCase {

    void registerClothes(Long memberId, ClothesRegisterCommand clothesRegisterCommand);

    ClothesResponses getClothes(Long memberId, String category, Set<String> season, Pageable pageable);

    void deleteClothes(Long memberId, Long clothesId);
}
