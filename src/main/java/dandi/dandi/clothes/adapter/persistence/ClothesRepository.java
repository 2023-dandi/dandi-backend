package dandi.dandi.clothes.adapter.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClothesRepository extends JpaRepository<ClothesJpaEntity, Long> {

    @Query("SELECT c FROM ClothesJpaEntity c JOIN FETCH c.seasons WHERE c.id = :id")
    Optional<ClothesJpaEntity> findById(Long id);
}
