package dandi.dandi.auth.infrastructure.apple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.domain.JwtParser;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.auth.infrastructure.apple.dto.ApplePublicKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.PublicKey;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AppleOAuthClientTest {

    private static final String ANY_TOKEN = "anyToken";

    private final JwtParser jwtParser = Mockito.mock(JwtParser.class);
    private final AppleApiCaller appleApiCaller = Mockito.mock(AppleApiCaller.class);
    private final AppleOAuthPublicKeyGenerator oAuthPublicKeyGenerator = Mockito.mock(
            AppleOAuthPublicKeyGenerator.class);
    private final AppleJwtClaimValidator appleJwtClaimValidator = Mockito.mock(AppleJwtClaimValidator.class);

    private final AppleOAuthClient appleOAuthClient =
            new AppleOAuthClient(jwtParser, appleApiCaller, oAuthPublicKeyGenerator, appleJwtClaimValidator);

    @DisplayName("토큰을 받아 사용자 식별 값을 반환할 수 있다.")
    @Test
    void getOAuthMemberId() {
        // given
        String memberIdentifier = "memberId";
        mockPublicKey();
        Claims claims = Jwts.claims()
                .setSubject(memberIdentifier);
        when(jwtParser.parseClaims(anyString(), any(PublicKey.class)))
                .thenReturn(claims);

        when(appleJwtClaimValidator.isExpired(any()))
                .thenReturn(false);
        when(appleJwtClaimValidator.isValid(any()))
                .thenReturn(true);

        // when
        String actual = appleOAuthClient.getOAuthMemberId(ANY_TOKEN);

        // then
        assertThat(actual).isEqualTo(memberIdentifier);
    }

    @DisplayName("만료된 토큰으로 사용자 식별 값을 반환받으려 하면 예외를 발생시킨다.")
    @Test
    void getOAuthMemberId_ExpiredToken() {
        // given
        mockExternalDependency();

        when(appleJwtClaimValidator.isExpired(any()))
                .thenReturn(true);
        when(appleJwtClaimValidator.isValid(any()))
                .thenReturn(true);

        // when, then
        assertThatThrownBy(() -> appleOAuthClient.getOAuthMemberId(ANY_TOKEN))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.expired().getMessage());
    }

    private void mockExternalDependency() {
        mockPublicKey();
        when(jwtParser.parseClaims(anyString(), any(PublicKey.class)))
                .thenReturn(Mockito.mock(Claims.class));
    }

    @DisplayName("iss, sub, nonce 값이 유효하지 않은 토큰으로 사용자 식별 값을 반환받으려 하면 예외를 발생시킨다.")
    @Test
    void getOAuthMemberId_InvalidToken() {
        // given
        mockExternalDependency();

        when(appleJwtClaimValidator.isExpired(any()))
                .thenReturn(false);
        when(appleJwtClaimValidator.isValid(any()))
                .thenReturn(false);

        // when, then
        assertThatThrownBy(() -> appleOAuthClient.getOAuthMemberId(ANY_TOKEN))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.invalid().getMessage());
    }

    private void mockPublicKey() {
        when(jwtParser.parseHeaders(anyString()))
                .thenReturn(Mockito.mock(Map.class));
        when(appleApiCaller.getPublicKeys())
                .thenReturn(Mockito.mock(ApplePublicKeys.class));
        when(oAuthPublicKeyGenerator.generatePublicKey(any(), any()))
                .thenReturn(Mockito.mock(PublicKey.class));
    }
}
