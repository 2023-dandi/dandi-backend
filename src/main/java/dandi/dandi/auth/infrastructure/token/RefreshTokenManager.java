package dandi.dandi.auth.infrastructure.token;

import dandi.dandi.auth.domain.RefreshToken;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenManager {

    private final long validDuration;

    public RefreshTokenManager(@Value(("${security.jwt.refresh.valid-duration}")) long validDuration) {
        this.validDuration = validDuration;
    }

    public RefreshToken generateToken(Long memberId) {
        int days = (int) TimeUnit.MILLISECONDS.toDays(validDuration);
        LocalDateTime expired = LocalDateTime.now().plusDays(days);
        return RefreshToken.generateNewWithExpiration(memberId, expired);
    }
}
