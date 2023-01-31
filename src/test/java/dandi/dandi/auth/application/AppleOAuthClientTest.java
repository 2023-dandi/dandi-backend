package dandi.dandi.auth.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AppleOAuthClientTest {

    private static final String ANY_TOKEN = "anyToken";

    private final JwtParser jwtParser = Mockito.mock(JwtParser.class);
    private final AppleOAuthPublicKeyGenerator oAuthPublicKeyGenerator = Mockito.mock(
            AppleOAuthPublicKeyGenerator.class);
    private final AppleJwtClaimValidator appleJwtClaimValidator = Mockito.mock(AppleJwtClaimValidator.class);

    private final AppleOAuthClient appleOAuthClient =
            new AppleOAuthClient(jwtParser, oAuthPublicKeyGenerator, appleJwtClaimValidator);

    @DisplayName("만료된 토큰으로 사용자 식별 값을 반환받으려 하면 예외를 발생시킨다.")
    @Test
    void getMemberIdentifier_ExpiredToken() {
        // given
        when(jwtParser.parseHeaders(anyString()))
                .thenReturn(Mockito.mock(Map.class));
        when(oAuthPublicKeyGenerator.generatePublicKey(any()))
                .thenReturn(Mockito.mock(PublicKey.class));
        when(jwtParser.parseClaims(anyString(), any(PublicKey.class)))
                .thenReturn(Mockito.mock(Claims.class));

        when(appleJwtClaimValidator.isExpired(any()))
                .thenReturn(true);
        when(appleJwtClaimValidator.isValid(any()))
                .thenReturn(true);

        // when, then
        assertThatThrownBy(() -> appleOAuthClient.getMemberIdentifier(ANY_TOKEN))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("만료된 토큰입니다.");
    }

    @DisplayName("iss, sub, nonce 값이 유효하지 않은 토큰으로 사용자 식별 값을 반환받으려 하면 예외를 발생시킨다.")
    @Test
    void getMemberIdentifier_InvalidToken() {
        // given
        when(jwtParser.parseHeaders(anyString()))
                .thenReturn(Mockito.mock(Map.class));
        when(oAuthPublicKeyGenerator.generatePublicKey(any()))
                .thenReturn(Mockito.mock(PublicKey.class));
        when(jwtParser.parseClaims(anyString(), any(PublicKey.class)))
                .thenReturn(Mockito.mock(Claims.class));

        when(appleJwtClaimValidator.isExpired(any()))
                .thenReturn(false);
        when(appleJwtClaimValidator.isValid(any()))
                .thenReturn(false);

        // when, then
        assertThatThrownBy(() -> appleOAuthClient.getMemberIdentifier(ANY_TOKEN))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }
}
