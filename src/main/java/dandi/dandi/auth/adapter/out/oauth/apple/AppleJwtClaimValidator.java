package dandi.dandi.auth.adapter.out.oauth.apple;

import io.jsonwebtoken.Claims;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleJwtClaimValidator {

    private final String issuer;
    private final String clientId;

    public AppleJwtClaimValidator(@Value("${oauth.apple.iss}") String issuer,
                                  @Value("${oauth.apple.client-id}") String clientId) {
        this.issuer = issuer;
        this.clientId = clientId;
    }

    public boolean isExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        Date currentDate = new Date();
        return expiration.before(currentDate);
    }

    public boolean isValid(Claims claims) {
        return claims.getIssuer().equals(issuer) &&
                claims.getAudience().equals(clientId);
    }
}
