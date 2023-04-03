package dandi.dandi.clothes.adapter.persistence;

import dandi.dandi.clothes.application.port.out.persistence.CategorySeasonProjection;
import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Season;
import java.util.List;
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
            + "WHERE c.memberId = :memberId AND c.category IN :categories AND cs.season IN :seasons")
    Slice<ClothesJpaEntity> findByMemberIdAndCategoryAndSeasons(Long memberId, Set<Category> categories,
                                                                Set<Season> seasons, Pageable pageable);

    //@Query("SELECT c FROM ClothesJpaEntity c INNER JOIN ClothesSeasonJpaEntity cs ON cs.clothesJpaEntity.id = c.id "
    //        + "WHERE c.memberId = :memberId GROUP BY c.category, cs.season")
    //@Query("SELECT c FROM ClothesJpaEntity c JOIN FETCH ClothesSeasonJpaEntity cs "
    //        + "WHERE c.memberId = :memberId GROUP BY c.category, cs.season")
    @Query("SELECT DISTINCT c.category as category, cs.season as season FROM ClothesJpaEntity c "
            + "INNER JOIN ClothesSeasonJpaEntity cs ON cs.clothesJpaEntity.id = c.id "
            + "WHERE c.memberId = :memberId")
    List<CategorySeasonProjection> findAllByCategoryDistinct(Long memberId);
}
