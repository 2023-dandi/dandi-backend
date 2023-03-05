package dandi.dandi.auth.application.port.out;

import dandi.dandi.auth.domain.RefreshToken;
import java.util.Optional;

public interface RefreshTokenPersistencePort {

    Optional<RefreshToken> findRefreshTokenByMemberIdAndValue(Long memberId, String value);

    void deleteByMemberId(Long memberId);

    RefreshToken save(RefreshToken refreshToken);
}
