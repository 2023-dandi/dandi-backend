package dandi.dandi.batchcommons.adapter.out.persistence.jpa.batchthreadsize;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BatchThreadSizeRepository extends JpaRepository<BatchThreadSizeJpaEntity, String> {

    Optional<BatchThreadSizeJpaEntity> findByName(String name);

    @Modifying
    @Query("UPDATE BatchThreadSizeJpaEntity bts SET bts.value = :chunkSize WHERE bts.name = :name")
    void updateChunkSizeByName(String name, int batchThreadSize);
}
