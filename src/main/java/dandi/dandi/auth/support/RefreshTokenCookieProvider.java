package dandi.dandi.auth.support;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieProvider {

    protected static final String REFRESH_TOKEN = "refreshToken";

    private final Long validDuration;

    public RefreshTokenCookieProvider(@Value("${security.refresh.valid-duration}") Long validDuration) {
        this.validDuration = validDuration;
    }

    public ResponseCookie createCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(SameSite.NONE.attributeValue())
                .maxAge(Duration.ofMillis(validDuration))
                .build();
    }
}

