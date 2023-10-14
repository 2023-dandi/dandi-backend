package dandi.dandi.batchcommons.adapter.out.persistence.jpa.batchthreadsize;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BatchThreadSizeRepository extends JpaRepository<BatchThreadSizeJpaEntity, String> {

    Optional<BatchThreadSizeJpaEntity> findByName(String name);

    @Modifying
    @Query("UPDATE BatchThreadSizeJpaEntity bts SET bts.value = :batchThreadSize WHERE bts.name = :name")
    void updateBatchThreadSizeByName(String name, int batchThreadSize);
}
