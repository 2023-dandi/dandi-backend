package dandi.dandi.auth.infrastructure.token;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.nio.charset.StandardCharsets.UTF_8;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager {

    private final SecretKey key;
    private final long validDuration;

    public JwtTokenManager(@Value("${security.jwt.secret-key}") String secretKey,
                           @Value("${security.jwt.valid-duration}") long validDuration) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(UTF_8));
        this.validDuration = validDuration;
    }

    public String generateToken(String payload) {
        Date current = new Date();
        Date expirationDate = new Date(current.getTime() + validDuration);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(current)
                .setExpiration(expirationDate)
                .signWith(key, HS256)
                .compact();
    }

    public Object getPayload(String token) {
        String payload = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.parseLong(payload);
    }
}
