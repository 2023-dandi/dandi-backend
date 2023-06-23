package dandi.dandi.auth.web.support;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.auth.adapter.in.web.support.AuthorizationExtractor;
import dandi.dandi.auth.exception.UnauthorizedException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AuthorizationExtractorTest {

    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private final Enumeration<String> authorizationHeaders = Mockito.mock(Enumeration.class);
    private static final String AUTHENTICATION_TYPE = "Bearer ";

    private final AuthorizationExtractor authorizationExtractor = new AuthorizationExtractor();

    @DisplayName("request의 Authorization 헤더에 Bearer 인증 타입를 포함한 AccessToken이 있다면 AccessToken을 추출한다.")
    @Test
    void extractAccessToken() {
        String accessToken = "access-token";
        String accessTokenWithAuthenticationType = AUTHENTICATION_TYPE + accessToken;
        when(request.getHeaders(AUTHORIZATION))
                .thenReturn(authorizationHeaders);
        when(authorizationHeaders.hasMoreElements())
                .thenReturn(true);
        when(authorizationHeaders.nextElement())
                .thenReturn(accessTokenWithAuthenticationType);

        String extractedAccessToken = authorizationExtractor.extractAccessToken(request);

        assertThat(extractedAccessToken).isEqualTo(accessToken);
    }

    @DisplayName("request의 Authorization 헤더에 AccessToken이 Bearer 인증 타입을 포함하지 않는다면 예외를 발생시킨다.")
    @Test
    void extractAccessToken_NotFoundAuthenticationType() {
        String accessTokenWithoutAuthenticationType = "access-token";
        when(request.getHeaders(AUTHORIZATION))
                .thenReturn(authorizationHeaders);
        when(authorizationHeaders.nextElement())
                .thenReturn(accessTokenWithoutAuthenticationType);

        assertThatThrownBy(() -> authorizationExtractor.extractAccessToken(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.accessTokenNotFound().getMessage());
    }
}
