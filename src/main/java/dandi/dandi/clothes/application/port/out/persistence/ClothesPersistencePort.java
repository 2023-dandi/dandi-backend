package dandi.dandi.clothes.application.port.out.persistence;

import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ClothesPersistencePort {

    void save(Clothes clothes);

    Optional<Clothes> findById(Long id);

    Slice<Clothes> findByMemberIdAndCategoryAndSeasons(Long memberId, Set<Category> categories,
                                                       Set<Season> seasons, Pageable pageable);

    void deleteById(Long clothesId);
}
