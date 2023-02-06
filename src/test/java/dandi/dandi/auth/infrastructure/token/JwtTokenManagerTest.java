package dandi.dandi.auth.infrastructure.token;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenManagerTest {

    private static final String SECRET = "secretSecretSecretSecretSecretSecretSecretSecretSecret";
    private static final long VALID_DURATION = 3600000;
    private final JwtTokenManager jwtTokenManager = new JwtTokenManager(SECRET, VALID_DURATION);

    @DisplayName("payload를 받아 토큰을 생성할 수 있다.")
    @Test
    void a() {
        String payload = "123";
        String token = jwtTokenManager.generateToken(payload);
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(UTF_8));

        String payloadFromToken = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertThat(payload).isEqualTo(payloadFromToken);
    }
}