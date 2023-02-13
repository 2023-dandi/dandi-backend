package dandi.dandi.member.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPatchWithAuthorization;
import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.common.HttpMethodFixture;
import dandi.dandi.member.application.dto.MemberInfoResponse;
import dandi.dandi.member.application.dto.NicknameUpdateRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MemberAcceptanceTest extends AcceptanceTest {

    private static final String LOGIN_REQUEST_URI = "/login/oauth/apple";
    private static final String MEMBER_INFO_URI = "/members";

    @DisplayName("회원 정보 요청에 대해 닉네임과 200을 반환한다.")
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

        MemberInfoResponse memberInfoResponse = response.jsonPath()
                .getObject(".", MemberInfoResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberInfoResponse.getNickname()).isEqualTo(memberInfoResponse.getNickname()),
                () -> assertThat(memberInfoResponse.getLatitude()).isEqualTo(initialLatitude),
                () -> assertThat(memberInfoResponse.getLongitude()).isEqualTo(initialLongitude)
        );
    }

    @DisplayName("회원 닉네임 변경 요청에 성공하면 204를 반환한다.")
    @Test
    void updateMemberNickname() {
        String oAuthIdToken = "idToken";
        when(oAuthClient.getOAuthMemberId(oAuthIdToken))
                .thenReturn("memberIdentifier");
        String token = HttpMethodFixture.httpPost(new LoginRequest(oAuthIdToken), LOGIN_REQUEST_URI)
                .header(AUTHORIZATION);
        String newNickname = "newNickname";

        ExtractableResponse<Response> response =
                httpPatchWithAuthorization("/members/nickname", new NicknameUpdateRequest(newNickname), token);

        String nicknameAfterNicknameUpdateRequest = httpGetWithAuthorization(MEMBER_INFO_URI, token)
                .jsonPath()
                .getObject(".", MemberInfoResponse.class)
                .getNickname();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(nicknameAfterNicknameUpdateRequest).isEqualTo(newNickname)
        );
    }

    @DisplayName("규칙에 어긋나는 회원 닉네임 변경 요청에 대해 400을 반환한다.")
    @Test
    void updateMemberNickname_BadRequest() {
        String oAuthIdToken = "idToken";
        when(oAuthClient.getOAuthMemberId(oAuthIdToken))
                .thenReturn("memberIdentifier");
        String token = HttpMethodFixture.httpPost(new LoginRequest(oAuthIdToken), LOGIN_REQUEST_URI)
                .header(AUTHORIZATION);
        String invalidNickname = "invalid  Nickname";

        ExtractableResponse<Response> response =
                httpPatchWithAuthorization("/members/nickname", new NicknameUpdateRequest(invalidNickname), token);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(extractExceptionMessage(response)).isNotNull()
        );
    }
}
