package dandi.dandi.auth.infrastructure.apple;

import dandi.dandi.auth.domain.JwtParser;
import dandi.dandi.auth.domain.OAuthClient;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.auth.infrastructure.apple.dto.ApplePublicKeys;
import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AppleOAuthClient implements OAuthClient {

    private final JwtParser jwtParser;
    private final AppleApiCaller appleApiCaller;
    private final AppleOAuthPublicKeyGenerator appleOAuthPublicKeyGenerator;
    private final AppleJwtClaimValidator appleJwtClaimValidator;

    public AppleOAuthClient(JwtParser jwtParser, AppleApiCaller appleApiCaller,
                            AppleOAuthPublicKeyGenerator appleOAuthPublicKeyGenerator,
                            AppleJwtClaimValidator appleJwtClaimValidator) {
        this.jwtParser = jwtParser;
        this.appleApiCaller = appleApiCaller;
        this.appleOAuthPublicKeyGenerator = appleOAuthPublicKeyGenerator;
        this.appleJwtClaimValidator = appleJwtClaimValidator;
    }

    @Override
    public String getOAuthMemberId(String idToken) {
        Map<String, String> tokenHeaders = jwtParser.parseHeaders(idToken);
        ApplePublicKeys applePublicKeys = appleApiCaller.getPublicKeys();
        PublicKey publicKey = appleOAuthPublicKeyGenerator.generatePublicKey(tokenHeaders, applePublicKeys);
        Claims claims = jwtParser.parseClaims(idToken, publicKey);
        validateClaims(claims);
        return claims.getSubject();
    }

    private void validateClaims(Claims claims) {
        if (appleJwtClaimValidator.isExpired(claims)) {
            throw UnauthorizedException.expired();
        }
        if (!appleJwtClaimValidator.isValid(claims)) {
            throw UnauthorizedException.rigged();
        }
    }
}
