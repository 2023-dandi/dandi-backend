package dandi.dandi.auth.infrastructure.token;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import dandi.dandi.auth.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenManagerTest {

    private static final String PAYLOAD = "123";
    private static final String SECRET = "secretSecretSecretSecretSecretSecretSecretSecretSecret";
    private static final long VALID_DURATION = 3600000;
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(UTF_8));

    private final JwtTokenManager jwtTokenManager = new JwtTokenManager(SECRET, VALID_DURATION);

    @DisplayName("payload를 받아 token을 생성할 수 있다.")
    @Test
    void generateToken() {
        String token = jwtTokenManager.generateToken(PAYLOAD);

        String payloadFromToken = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
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

    @DisplayName("조작된 토큰을 검증하면 예외를 발생시킨다.")
    @Test
    void validate_RiggedToken() {
        assertThatThrownBy(() -> jwtTokenManager.validate("riggedToken"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.rigged().getMessage());
    }

    @DisplayName("만료된 토큰을 검증하면 예외를 발생시킨다.")
    @Test
    void validate_ExpiredToken() {
        Date current = new Date();
        Date expiredDate = new Date(current.getTime() - 1);
        String expiredToken = generateExpiredToken(current, expiredDate);

        assertThatThrownBy(() -> jwtTokenManager.validate(expiredToken))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.expired().getMessage());
    }

    private String generateExpiredToken(Date current, Date expiredDate) {
        return Jwts.builder()
                .setSubject(PAYLOAD)
                .setIssuedAt(current)
                .setExpiration(expiredDate)
                .signWith(SECRET_KEY, HS256)
                .compact();
    }
}
