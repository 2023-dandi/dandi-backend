package dandi.dandi.auth.adapter.out.oauth.apple;

import dandi.dandi.auth.adapter.out.jwt.JwtParser;
import dandi.dandi.auth.adapter.out.oauth.apple.client.AppleApiCaller;
import dandi.dandi.auth.adapter.out.oauth.apple.dto.ApplePublicKeys;
import dandi.dandi.auth.application.port.out.oauth.OAuthClientPort;
import dandi.dandi.auth.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;

@Component
public class AppleOAuthClientAdapter implements OAuthClientPort {

    private final JwtParser jwtParser;
    private final AppleApiCaller appleApiCaller;
    private final AppleOAuthPublicKeyGenerator appleOAuthPublicKeyGenerator;
    private final AppleJwtClaimValidator appleJwtClaimValidator;

    public AppleOAuthClientAdapter(JwtParser jwtParser, AppleApiCaller appleApiCaller,
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
