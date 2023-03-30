package dandi.dandi.clothes.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesRepository extends JpaRepository<ClothesJpaEntity, Long> {
}
