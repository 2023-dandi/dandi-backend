package dandi.dandi.auth.acceptance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.common.HttpMethodFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String LOGIN_REQUEST_URI = "/login/oauth/apple";

    @DisplayName("처음으로 로그인하는 사용자가 oauth 로그인을 하면 회원 가입을 진행하고 201과 token을 반환한다.")
    @Test
    void login_NewMember() {
        String oAuthIdToken = "idToken";
        when(oAuthClient.getOAuthMemberId(oAuthIdToken))
                .thenReturn("memberIdentifier");

        ExtractableResponse<Response> response =
                HttpMethodFixture.httpPost(new LoginRequest(oAuthIdToken), LOGIN_REQUEST_URI);

        String token = response.header(HttpHeaders.AUTHORIZATION);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(token).isNotNull()
        );
    }

    @DisplayName("기존 사용자가 oauth 로그인을 하면 200과 token을 반환한다.")
    @Test
    void login_ExistingMember() {
        String oAuthIdToken = "idToken";
        when(oAuthClient.getOAuthMemberId(oAuthIdToken))
                .thenReturn("memberIdentifier");
        HttpMethodFixture.httpPost(new LoginRequest(oAuthIdToken), LOGIN_REQUEST_URI);

        ExtractableResponse<Response> response =
                HttpMethodFixture.httpPost(new LoginRequest(oAuthIdToken), LOGIN_REQUEST_URI);

        String token = response.header(HttpHeaders.AUTHORIZATION);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(token).isNotNull()
        );
    }
}
