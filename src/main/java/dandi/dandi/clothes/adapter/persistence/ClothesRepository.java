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

    @Query("SELECT DISTINCT c.category as category, cs.season as season FROM ClothesJpaEntity c "
            + "INNER JOIN ClothesSeasonJpaEntity cs ON cs.clothesJpaEntity.id = c.id "
            + "WHERE c.memberId = :memberId")
    List<CategorySeasonProjection> findAllByCategoryDistinct(Long memberId);

    @Query("SELECT COUNT(DISTINCT c.category) FROM ClothesJpaEntity c "
            + "INNER JOIN c.seasons cs ON c.id = cs.clothesJpaEntity.id "
            + "WHERE cs.season IN :seasons AND c.memberId = :memberId")
    int countDistinctCategoryByMemberIdAndSeasons(Long memberId, Set<Season> seasons);

    @Query("SELECT DISTINCT c "
            + "FROM ClothesJpaEntity c "
            + "         JOIN FETCH ClothesSeasonJpaEntity cs ON c.id = cs.clothesJpaEntity.id "
            + "WHERE c.memberId IN (SELECT DISTINCT c.memberId "
            + "                      FROM ClothesJpaEntity c "
            + "                               INNER JOIN ClothesSeasonJpaEntity cs "
            + "                                          ON c.id = cs.clothesJpaEntity.id AND cs.season IN :seasons "
            + "                      WHERE c.memberId = :memberId"
            + "                      GROUP BY c.memberId"
            + "                      HAVING COUNT(DISTINCT c.category) >= :categoriesCount) "
            + "  AND c.id IN (SELECT c.id FROM ClothesJpaEntity c "
            + "                                INNER JOIN ClothesSeasonJpaEntity cs ON c.id = cs.clothesJpaEntity.id "
            + "                       WHERE c.memberId = :memberId "
            + "                         AND cs.season IN :seasons)")
    Slice<ClothesJpaEntity> findByMemberIdAndSeasons(Long memberId, long categoriesCount,
                                                     Set<Season> seasons, Pageable pageable);
}
