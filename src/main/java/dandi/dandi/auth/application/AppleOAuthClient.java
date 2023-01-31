package dandi.dandi.auth.application;

import dandi.dandi.auth.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AppleOAuthClient implements OAuthClient {

    private final JwtParser jwtParser;
    private final AppleOAuthPublicKeyGenerator appleOAuthPublicKeyGenerator;
    private final AppleJwtClaimValidator appleJwtClaimValidator;

    public AppleOAuthClient(JwtParser jwtParser, AppleOAuthPublicKeyGenerator appleOAuthPublicKeyGenerator,
                            AppleJwtClaimValidator appleJwtClaimValidator) {
        this.jwtParser = jwtParser;
        this.appleOAuthPublicKeyGenerator = appleOAuthPublicKeyGenerator;
        this.appleJwtClaimValidator = appleJwtClaimValidator;
    }

    public String getMemberIdentifier(String idToken) {
        Map<String, String> tokenHeaders = jwtParser.parseHeaders(idToken);
        PublicKey publicKey = appleOAuthPublicKeyGenerator.generatePublicKey(tokenHeaders);
        Claims claims = jwtParser.parseClaims(idToken, publicKey);
        validateClaims(claims);
        return claims.getSubject();
    }

    private void validateClaims(Claims claims) {
        if (appleJwtClaimValidator.isExpired(claims)) {
            throw new UnauthorizedException("만료된 토큰입니다.");
        }
        if (!appleJwtClaimValidator.isValid(claims)) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}
