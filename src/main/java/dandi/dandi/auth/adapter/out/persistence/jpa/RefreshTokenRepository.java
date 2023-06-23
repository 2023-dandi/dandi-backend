package dandi.dandi.auth.adapter.out.persistence.jpa;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

    Optional<RefreshTokenJpaEntity> findByValue(String value);

    void deleteByMemberId(Long memberId);

    @Modifying
    @Query("UPDATE RefreshTokenJpaEntity rt SET rt.expired = :expired, rt.value = :value WHERE rt.id = :id")
    void update(Long id, LocalDateTime expired, String value);
}
