package dandi.dandi.clothes.adapter.persistence;

import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Season;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClothesRepository extends JpaRepository<ClothesJpaEntity, Long> {

    @Query("SELECT c FROM ClothesJpaEntity c JOIN FETCH c.seasons WHERE c.id = :id")
    Optional<ClothesJpaEntity> findById(Long id);

    @Query("SELECT DISTINCT c FROM ClothesJpaEntity c "
            + "INNER JOIN ClothesSeasonJpaEntity cs ON cs.clothesJpaEntity.id = c.id "
            + "WHERE c.memberId = :memberId AND c.category = :category AND cs.season IN :seasons")
    Slice<ClothesJpaEntity> findByMemberIdAndCategoryAndSeasons(Long memberId, Category category,
                                                                Set<Season> seasons, Pageable pageable);
}
