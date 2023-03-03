package dandi.dandi.auth.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findRefreshTokenByMemberIdAndValue(Long memberId, String value);

    void deleteByMemberId(Long memberId);
}
