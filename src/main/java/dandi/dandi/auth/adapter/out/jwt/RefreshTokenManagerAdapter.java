package dandi.dandi.auth.adapter.out.jwt;

import dandi.dandi.auth.application.port.out.jwt.RefreshTokenManagerPort;
import dandi.dandi.auth.domain.RefreshToken;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenManagerAdapter implements RefreshTokenManagerPort {

    private final long validDuration;

    public RefreshTokenManagerAdapter(@Value(("${security.jwt.refresh.valid-duration}")) long validDuration) {
        this.validDuration = validDuration;
    }

    @Override
    public RefreshToken generateToken(Long memberId) {
        int days = (int) TimeUnit.MILLISECONDS.toDays(validDuration);
        LocalDateTime expired = LocalDateTime.now().plusDays(days);
        return RefreshToken.generateNewWithExpiration(memberId, expired);
    }
}
