package dandi.dandi.batchcommons.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChunkSizeRepository extends JpaRepository<ChunkSizeJpaEntity, Long> {

    Optional<ChunkSizeJpaEntity> findByName(String name);
}
