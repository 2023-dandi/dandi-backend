package dandi.dandi.batchcommons.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChunkSizeRepository extends JpaRepository<ChunkSizeJpaEntity, Long> {

    Optional<ChunkSizeJpaEntity> findByName(String name);

    @Modifying
    @Query("UPDATE ChunkSizeJpaEntity cs SET cs.value = :chunkSize WHERE cs.name = :name")
    void updateChunkSizeByName(String name, int chunkSize);
}
