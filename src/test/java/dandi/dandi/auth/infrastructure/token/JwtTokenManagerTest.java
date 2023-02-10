package dandi.dandi.auth.infrastructure.token;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenManagerTest {

    private static final String PAYLOAD = "123";
    private static final String SECRET = "secretSecretSecretSecretSecretSecretSecretSecretSecret";
    private static final long VALID_DURATION = 3600000;
    private final JwtTokenManager jwtTokenManager = new JwtTokenManager(SECRET, VALID_DURATION);

    @DisplayName("payload를 받아 token을 생성할 수 있다.")
    @Test
    void generateToken() {
        String token = jwtTokenManager.generateToken(PAYLOAD);
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(UTF_8));

        String payloadFromToken = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertThat(PAYLOAD).isEqualTo(payloadFromToken);
    }

    @DisplayName("token을 받아 payload를 반환할 수 있다.")
    @Test
    void getPayload() {
        String token = jwtTokenManager.generateToken(PAYLOAD);

        Long payload = (Long) jwtTokenManager.getPayload(token);

        assertThat(payload).isEqualTo(Long.parseLong(PAYLOAD));
    }
}