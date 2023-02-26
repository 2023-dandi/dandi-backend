package dandi.dandi.auth.acceptance;

import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static dandi.dandi.common.RequestURI.LOGIN_REQUEST_URI;
import static dandi.dandi.common.RequestURI.TOKEN_REFRESH_REQUEST_URI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.common.HttpMethodFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String VALID_OAUTH_ID_TOKEN = "oAuthIdToken";
    private static final String REFRESH_TOKEN = "Refresh-Token";

    @DisplayName("처음으로 로그인하는 사용자가 oauth 로그인을 하면 회원 가입을 진행하고 201과 access, refresh token을 반환한다.")
    @Test
    void login_NewMember() {
        mockAppleIdToken(VALID_OAUTH_ID_TOKEN);

        ExtractableResponse<Response> response =
                HttpMethodFixture.httpPost(new LoginRequest(VALID_OAUTH_ID_TOKEN), LOGIN_REQUEST_URI);

        String accessToken = response.header(HttpHeaders.AUTHORIZATION);
        String setCookie = response.header(HttpHeaders.SET_COOKIE);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(accessToken).isNotNull(),
                () -> assertThat(setCookie).contains(REFRESH_TOKEN)
        );
    }

    @DisplayName("기존 사용자가 oauth 로그인을 하면 200과 access, refresh token을 반환한다.")
    @Test
    void login_ExistingMember() {
        mockAppleIdToken(VALID_OAUTH_ID_TOKEN);
        HttpMethodFixture.httpPost(new LoginRequest(VALID_OAUTH_ID_TOKEN), LOGIN_REQUEST_URI);

        ExtractableResponse<Response> response =
                HttpMethodFixture.httpPost(new LoginRequest(VALID_OAUTH_ID_TOKEN), LOGIN_REQUEST_URI);

        String accessToken = response.header(HttpHeaders.AUTHORIZATION);
        String setCookie = response.header(HttpHeaders.SET_COOKIE);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(accessToken).isNotNull(),
                () -> assertThat(setCookie).contains(REFRESH_TOKEN)
        );
    }

    @DisplayName("만료된 Apple Identity Token으로 로그인/회원가입을 요청하면 401을 반환한다.")
    @Test
    void login_Unauthorized_ExpiredAppleIdToken() {
        String expiredToken = "expiredToken";
        mockExpiredToken(expiredToken);
        UnauthorizedException unauthorizedException = UnauthorizedException.expired();

        ExtractableResponse<Response> response = HttpMethodFixture.httpPost(new LoginRequest(expiredToken),
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
        UnauthorizedException unauthorizedException = UnauthorizedException.invalid();

        ExtractableResponse<Response> response = HttpMethodFixture.httpPost(new LoginRequest(invalidToken),
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
        ExtractableResponse<Response> loginResponse =
                HttpMethodFixture.httpPost(new LoginRequest(VALID_OAUTH_ID_TOKEN), LOGIN_REQUEST_URI);
        String accessTokenBeforeRefresh = loginResponse.header(AUTHORIZATION);
        String refreshTokenBeforeRefresh = loginResponse.cookie(REFRESH_TOKEN);

        ExtractableResponse<Response> response = HttpMethodFixture.httpPostWithAuthorizationAndCookie(
                TOKEN_REFRESH_REQUEST_URI, accessTokenBeforeRefresh, Map.of(REFRESH_TOKEN, refreshTokenBeforeRefresh));

        String accessTokenAfterRefresh = response.header(AUTHORIZATION);
        String refreshTokenAfterRefresh = response.cookie(REFRESH_TOKEN);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(accessTokenAfterRefresh).isNotNull(),
                () -> assertThat(refreshTokenAfterRefresh).isNotEqualTo(refreshTokenBeforeRefresh)
        );
    }

    private void mockAppleIdToken(String accessToken) {
        when(oAuthClient.getOAuthMemberId(accessToken))
                .thenReturn("memberIdentifier");
    }

    private void mockExpiredToken(String accessToken) {
        when(oAuthClient.getOAuthMemberId(accessToken))
                .thenThrow(UnauthorizedException.expired());
    }

    private void mockInvalidToken(String accessToken) {
        when(oAuthClient.getOAuthMemberId(accessToken))
                .thenThrow(UnauthorizedException.invalid());
    }
}
