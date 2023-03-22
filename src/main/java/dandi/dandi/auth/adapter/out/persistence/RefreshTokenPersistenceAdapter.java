package dandi.dandi.auth.adapter.out.persistence;

import dandi.dandi.auth.application.port.out.persistence.RefreshTokenPersistencePort;
import dandi.dandi.auth.domain.RefreshToken;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenPersistenceAdapter implements RefreshTokenPersistencePort {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenPersistenceAdapter(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(RefreshTokenJpaEntity.fromRefreshToken(refreshToken))
                .toRefreshToken();
    }

    @Override
    public Optional<RefreshToken> findByValue(String value) {
        return refreshTokenRepository.findByValue(value)
                .map(RefreshTokenJpaEntity::toRefreshToken);
    }

    @Override
    public void update(Long id, RefreshToken refreshToken) {
        refreshTokenRepository.update(id, refreshToken.getExpired(), refreshToken.getValue());
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }
}
