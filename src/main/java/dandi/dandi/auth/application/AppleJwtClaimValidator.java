package dandi.dandi.auth.application;

import io.jsonwebtoken.Claims;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleJwtClaimValidator {

    private static final String NONCE_KEY = "nonce";

    private final String issuer;
    private final String clientId;
    private final String nonce;

    public AppleJwtClaimValidator(@Value("${oauth.apple.iss}") String issuer,
                                  @Value("${oauth.apple.client-id}") String clientId,
                                  @Value("${oauth.apple.nonce}") String nonce) {
        this.issuer = issuer;
        this.clientId = clientId;
        this.nonce = nonce;
    }

    public boolean isExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        Date currentDate = new Date();
        return expiration.before(currentDate);
    }

    public boolean isValid(Claims claims) {
        return claims.getIssuer().equals(issuer) &&
                claims.getAudience().equals(clientId) &&
                claims.get(NONCE_KEY, String.class).equals(nonce);
    }
}
