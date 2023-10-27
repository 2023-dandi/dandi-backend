package dandi.dandi.auth.adapter.oauth.apple;

import dandi.dandi.auth.adapter.out.jwt.JwtParser;
import dandi.dandi.auth.adapter.out.oauth.apple.AppleJwtClaimValidator;
import dandi.dandi.auth.adapter.out.oauth.apple.AppleOAuthClientAdapter;
import dandi.dandi.auth.adapter.out.oauth.apple.AppleOAuthPublicKeyGenerator;
import dandi.dandi.auth.adapter.out.oauth.apple.client.AppleApiCaller;
import dandi.dandi.auth.adapter.out.oauth.apple.dto.ApplePublicKeys;
import dandi.dandi.auth.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.PublicKey;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AppleOAuthClientAdapterTest {

    private static final String ANY_TOKEN = "anyToken";

    private final JwtParser jwtParser = Mockito.mock(JwtParser.class);
    private final AppleApiCaller appleApiCaller = Mockito.mock(AppleApiCaller.class);
    private final AppleOAuthPublicKeyGenerator oAuthPublicKeyGenerator = Mockito.mock(
            AppleOAuthPublicKeyGenerator.class);
    private final AppleJwtClaimValidator appleJwtClaimValidator = Mockito.mock(AppleJwtClaimValidator.class);

    private final AppleOAuthClientAdapter appleOAuthClient =
            new AppleOAuthClientAdapter(jwtParser, appleApiCaller, oAuthPublicKeyGenerator, appleJwtClaimValidator);

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
                .hasMessage(UnauthorizedException.rigged().getMessage());
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
