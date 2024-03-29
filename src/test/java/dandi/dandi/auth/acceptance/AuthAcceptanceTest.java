package dandi.dandi.auth.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPost;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static dandi.dandi.common.RequestURI.LOGIN_REQUEST_URI;
import static dandi.dandi.common.RequestURI.LOGOUT_REQUEST_URI;
import static dandi.dandi.common.RequestURI.TOKEN_REFRESH_REQUEST_URI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.adapter.in.web.dto.LoginRequest;
import dandi.dandi.auth.application.port.in.TokenResponse;
import dandi.dandi.auth.domain.RefreshToken;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.common.HttpMethodFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String VALID_OAUTH_ID_TOKEN = "oAuthIdToken";
    private static final String PUSH_NOTIFICATION_TOKEN = "dasdaddsad";
    private static final String REFRESH_TOKEN = "Refresh-Token";

    @DisplayName("처음으로 로그인하는 사용자가 oauth 로그인을 하면 회원 가입을 진행하고 201과 access, refresh token을 반환한다.")
    @Test
    void login_NewMember() {
        mockAppleIdToken(VALID_OAUTH_ID_TOKEN);

        ExtractableResponse<Response> response =
                httpPost(new LoginRequest(VALID_OAUTH_ID_TOKEN, PUSH_NOTIFICATION_TOKEN), LOGIN_REQUEST_URI);

        TokenResponse tokenResponse = response.jsonPath()
                .getObject(".", TokenResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(tokenResponse.getAccessToken()).isNotNull(),
                () -> assertThat(tokenResponse.getRefreshToken()).isNotNull()
        );
    }

    @DisplayName("기존 사용자가 oauth 로그인을 하면 200과 access, refresh token을 반환한다.")
    @Test
    void login_ExistingMember() {
        mockAppleIdToken(VALID_OAUTH_ID_TOKEN);
        httpPost(new LoginRequest(VALID_OAUTH_ID_TOKEN, PUSH_NOTIFICATION_TOKEN), LOGIN_REQUEST_URI);

        ExtractableResponse<Response> response =
                httpPost(new LoginRequest(VALID_OAUTH_ID_TOKEN, PUSH_NOTIFICATION_TOKEN), LOGIN_REQUEST_URI);

        TokenResponse tokenResponse = response.jsonPath()
                .getObject(".", TokenResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(tokenResponse.getAccessToken()).isNotNull(),
                () -> assertThat(tokenResponse.getRefreshToken()).isNotNull()
        );
    }

    @DisplayName("만료된 Apple Identity Token으로 로그인/회원가입을 요청하면 401을 반환한다.")
    @Test
    void login_Unauthorized_ExpiredAppleIdToken() {
        String expiredToken = "expiredToken";
        mockExpiredToken(expiredToken);
        UnauthorizedException unauthorizedException = UnauthorizedException.expired();

        ExtractableResponse<Response> response = httpPost(new LoginRequest(expiredToken, PUSH_NOTIFICATION_TOKEN),
                LOGIN_REQUEST_URI);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(extractExceptionMessage(response)).isEqualTo(unauthorizedException.getMessage())
        );
    }

    @DisplayName("유효하지 않은 iss, client-id, nonce의 Apple Identity Token으로 로그인/회원가입을 요청하면 401을 반환한다.")
    @Test
    void login_Unauthorized_InvalidAppleIdToken() {
        String invalidToken = "invalidToken";
        mockInvalidToken(invalidToken);
        UnauthorizedException unauthorizedException = UnauthorizedException.rigged();

        ExtractableResponse<Response> response = httpPost(new LoginRequest(invalidToken, PUSH_NOTIFICATION_TOKEN),
                LOGIN_REQUEST_URI);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(extractExceptionMessage(response)).isEqualTo(unauthorizedException.getMessage())
        );
    }

    @DisplayName("Token Refresh 요청에 대해 200과 새로운 Access Token과 Refresh Token을 응답한다.")
    @Test
    void refresh() {
        mockAppleIdToken(VALID_OAUTH_ID_TOKEN);
        TokenResponse tokenResponse =
                httpPost(new LoginRequest(VALID_OAUTH_ID_TOKEN, PUSH_NOTIFICATION_TOKEN), LOGIN_REQUEST_URI)
                        .jsonPath()
                        .getObject(".", TokenResponse.class);
        String refreshTokenBeforeRefresh = tokenResponse.getRefreshToken();

        ExtractableResponse<Response> response = HttpMethodFixture.httpPostWithCookie(
                TOKEN_REFRESH_REQUEST_URI, Map.of(REFRESH_TOKEN, refreshTokenBeforeRefresh));

        TokenResponse tokenRefreshResponse = response.jsonPath()
                .getObject(".", TokenResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(tokenRefreshResponse.getAccessToken()).isNotNull(),
                () -> assertThat(tokenRefreshResponse.getRefreshToken()).isNotEqualTo(refreshTokenBeforeRefresh)
        );
    }

    @DisplayName("존재하지 않는 RefreshToken을 통한 Token Refresh 요청에 대해 401을 응답한다.")
    @Test
    void refresh_NotFountRefreshToken() {
        mockAppleIdToken(VALID_OAUTH_ID_TOKEN);
        String notFoundRefreshToken = "notFoundRefreshToken";

        ExtractableResponse<Response> response = HttpMethodFixture.httpPostWithCookie(
                TOKEN_REFRESH_REQUEST_URI, Map.of(REFRESH_TOKEN, notFoundRefreshToken));
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(extractExceptionMessage(response))
                        .isEqualTo(UnauthorizedException.refreshTokenNotFound().getMessage())
        );
    }

    @DisplayName("만료된 Refresh 토큰을 통한 Token Refresh에 대해 401을 응답한다.")
    @Test
    void refresh_ExpiredRefreshToken() {
        mockAppleIdToken(VALID_OAUTH_ID_TOKEN);
        mockExpiredRefreshToken();
        TokenResponse tokenResponse =
                httpPost(new LoginRequest(VALID_OAUTH_ID_TOKEN, PUSH_NOTIFICATION_TOKEN), LOGIN_REQUEST_URI)
                        .jsonPath()
                        .getObject(".", TokenResponse.class);
        String refreshTokenBeforeRefresh = tokenResponse.getRefreshToken();

        ExtractableResponse<Response> response = HttpMethodFixture.httpPostWithCookie(
                TOKEN_REFRESH_REQUEST_URI, Map.of(REFRESH_TOKEN, refreshTokenBeforeRefresh));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(extractExceptionMessage(response))
                        .isEqualTo(UnauthorizedException.expiredRefreshToken().getMessage())
        );
    }

    @DisplayName("로그아웃 요청에 성공하면 204를 반환한다.")
    @Test
    void logout() {
        String token = getToken();

        ExtractableResponse<Response> response = httpPostWithAuthorization(LOGOUT_REQUEST_URI, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("조작된 Access Token으로 요청하면 401을 반환한다.")
    @Test
    void accessToken_RiggedAccessToken() {
        ExtractableResponse<Response> response = httpGetWithAuthorization("/members", "riggedAccessToken");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


    private void mockAppleIdToken(String accessToken) {
        when(oAuthClientPort.getOAuthMemberId(accessToken))
                .thenReturn("memberIdentifier");
    }

    private void mockExpiredToken(String accessToken) {
        when(oAuthClientPort.getOAuthMemberId(accessToken))
                .thenThrow(UnauthorizedException.expired());
    }

    private void mockInvalidToken(String accessToken) {
        when(oAuthClientPort.getOAuthMemberId(accessToken))
                .thenThrow(UnauthorizedException.rigged());
    }

    private void mockExpiredRefreshToken() {
        when(refreshTokenManager.generateToken(any()))
                .thenReturn(RefreshToken.generateNewWithExpiration(1L, LocalDateTime.now().minusDays(1)));
    }
}
