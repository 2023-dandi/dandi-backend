package dandi.dandi.clothes.application.port.in;

import java.time.LocalDate;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface ClothesQueryServicePort {

    ClothesDetailResponse getSingleClothesDetails(Long memberId, Long clothesId);

    ClothesResponses getClothes(Long memberId, String category, Set<String> season, Pageable pageable);

    CategorySeasonsResponses getCategoriesAndSeasons(Long memberId);

    ClothesResponses getTodayClothes(Long memberId, LocalDate today, Pageable pageable);
}
