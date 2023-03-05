package dandi.dandi.auth.adapter.out;

import dandi.dandi.auth.application.port.out.RefreshTokenPersistencePort;
import dandi.dandi.auth.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenPersistenceAdapter extends RefreshTokenPersistencePort,
        JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findRefreshTokenByMemberIdAndValue(Long memberId, String value);

    @Override
    void deleteByMemberId(Long memberId);
}
