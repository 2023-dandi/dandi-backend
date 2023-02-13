package dandi.dandi.member.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.common.HttpMethodFixture;
import dandi.dandi.member.application.dto.MemberInfoResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MemberAcceptanceTest extends AcceptanceTest {

    private static final String LOGIN_REQUEST_URI = "/login/oauth/apple";
    private static final String MEMBER_INFO_URI = "/members";

    @DisplayName("회원의 닉네임 반환 요청이 오면 닉네임과 200을 반환한다.")
    @Test
    void getMemberNickname() {
        String oAuthIdToken = "idToken";
        when(oAuthClient.getOAuthMemberId(oAuthIdToken))
                .thenReturn("memberIdentifier");
        String token = HttpMethodFixture.httpPost(new LoginRequest(oAuthIdToken), LOGIN_REQUEST_URI)
                .header(AUTHORIZATION);
        double initialLatitude = 0.0;
        double initialLongitude = 0.0;

        ExtractableResponse<Response> response = httpGetWithAuthorization(MEMBER_INFO_URI, token);

        MemberInfoResponse nicknameResponse = response.jsonPath()
                .getObject(".", MemberInfoResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(nicknameResponse.getNickname()).isEqualTo(nicknameResponse.getNickname()),
                () -> assertThat(nicknameResponse.getLatitude()).isEqualTo(initialLatitude),
                () -> assertThat(nicknameResponse.getLongitude()).isEqualTo(initialLongitude)
        );
    }
}
