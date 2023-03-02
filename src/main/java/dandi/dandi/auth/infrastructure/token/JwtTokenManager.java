package dandi.dandi.auth.infrastructure.token;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.nio.charset.StandardCharsets.UTF_8;

import dandi.dandi.auth.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager {

    private final SecretKey key;
    private final long validDuration;

    public JwtTokenManager(@Value("${security.jwt.access.secret-key}") String secretKey,
                           @Value("${security.jwt.access.valid-duration}") long validDuration) {
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

    public void validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw UnauthorizedException.invalid();
        } catch (ExpiredJwtException e) {
            throw UnauthorizedException.expired();
        }
    }

}
