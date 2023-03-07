package dandi.dandi.auth.application.port.out.jwt;

import dandi.dandi.auth.domain.RefreshToken;

public interface RefreshTokenManagerPort {
    RefreshToken generateToken(Long memberId);
}
