package dandi.dandi.member.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPatchWithAuthorization;
import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static dandi.dandi.common.RequestURI.MEMBER_INFO_URI;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_LOCATION;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_URI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.member.application.dto.LocationUpdateRequest;
import dandi.dandi.member.application.dto.MemberInfoResponse;
import dandi.dandi.member.application.dto.NicknameUpdateRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 정보 요청에 대해 닉네임과 200을 반환한다.")
    @Test
    void getMemberNickname() {
        String token = getToken();
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
        String token = getToken();
        String newNickname = "newNickname";

        ExtractableResponse<Response> response =
                httpPatchWithAuthorization(MEMBER_NICKNAME_URI, new NicknameUpdateRequest(newNickname), token);

        String nicknameAfterNicknameUpdateRequest = getNickname(token);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(nicknameAfterNicknameUpdateRequest).isEqualTo(newNickname)
        );
    }

    @DisplayName("규칙에 어긋나는 회원 닉네임 변경 요청에 대해 400을 반환한다.")
    @Test
    void updateMemberNickname_BadRequest() {
        String token = getToken();
        String invalidNickname = "invalid  Nickname";

        ExtractableResponse<Response> response =
                httpPatchWithAuthorization(MEMBER_NICKNAME_URI, new NicknameUpdateRequest(invalidNickname), token);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(extractExceptionMessage(response)).isNotNull()
        );
    }

    @DisplayName("사용자 위치 정보 변경에 성공하면 204를 반환한다.")
    @Test
    void updateMemberLocation_NoContent() {
        String token = getToken();
        LocationUpdateRequest locationUpdateRequest = new LocationUpdateRequest(1.0, 2.0);

        ExtractableResponse<Response> response =
                httpPatchWithAuthorization(MEMBER_NICKNAME_LOCATION, locationUpdateRequest, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("잘못된 범위의 사용자 위치 정보 요청에 대해 400을 반환한다.")
    @Test
    void updateMemberLocation_BadRequest() {
        String token = getToken();
        double invalidLatitude = -91.0;
        LocationUpdateRequest invalidLocationUpdateRequest = new LocationUpdateRequest(invalidLatitude, -2.0);

        ExtractableResponse<Response> response =
                httpPatchWithAuthorization(MEMBER_NICKNAME_LOCATION, invalidLocationUpdateRequest, token);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(extractExceptionMessage(response)).isNotNull()
        );
    }

    private String getNickname(String token) {
        return httpGetWithAuthorization(MEMBER_INFO_URI, token)
                .jsonPath()
                .getObject(".", MemberInfoResponse.class)
                .getNickname();
    }
}
