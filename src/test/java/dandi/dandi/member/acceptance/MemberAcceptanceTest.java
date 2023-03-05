package dandi.dandi.member.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpGet;
import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPatchWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPutWithAuthorizationAndImgFile;
import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static dandi.dandi.common.RequestURI.MEMBER_INFO_URI;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_DUPLICATION_CHECK_URI;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_LOCATION;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_URI;
import static dandi.dandi.common.RequestURI.MEMBER_PROFILE_IMAGE_URI;
import static dandi.dandi.utils.image.TestImageUtils.TEST_IMAGE_FILE_NAME;
import static dandi.dandi.utils.image.TestImageUtils.generatetestImgFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;

import com.amazonaws.AmazonClientException;
import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.member.application.port.in.dto.LocationUpdateRequest;
import dandi.dandi.member.application.port.in.dto.NicknameUpdateRequest;
import dandi.dandi.member.application.port.out.dto.MemberInfoResponse;
import dandi.dandi.member.application.port.out.dto.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.port.out.dto.ProfileImageUpdateResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
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
                () -> assertThat(memberInfoResponse.getLongitude()).isEqualTo(initialLongitude),
                () -> assertThat(memberInfoResponse.getProfileImageUrl()).isNotNull()
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

    @DisplayName("닉네임 중복 체크 요청에 대해 중복여부와 200을 반환한다.")
    @ParameterizedTest
    @CsvSource({"nickname123, true", "123nickname, false"})
    void checkNicknameDuplication(String nickname, boolean expected) {
        String token = getToken();
        httpPatchWithAuthorization(MEMBER_NICKNAME_URI, new NicknameUpdateRequest("nickname123"), token);

        ExtractableResponse<Response> response =
                httpGet(MEMBER_NICKNAME_DUPLICATION_CHECK_URI + "?nickname=" + nickname);

        boolean duplicated = response.jsonPath()
                .getObject(".", NicknameDuplicationCheckResponse.class)
                .isDuplicated();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(duplicated).isEqualTo(expected)
        );
    }

    @DisplayName("사용자 위치 정보 변경에 성공하면 204를 반환한다.")
    @Test
    void updateMemberLocation_NoContent() {
        String token = getToken();
        double latitude = 1.0;
        double longitude = 2.0;
        LocationUpdateRequest locationUpdateRequest = new LocationUpdateRequest(latitude, longitude);

        ExtractableResponse<Response> response =
                httpPatchWithAuthorization(MEMBER_NICKNAME_LOCATION, locationUpdateRequest, token);

        MemberInfoResponse memberInfoResponse = httpGetWithAuthorization(MEMBER_INFO_URI, token)
                .jsonPath()
                .getObject(".", MemberInfoResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(memberInfoResponse.getLatitude()).isEqualTo(latitude),
                () -> assertThat(memberInfoResponse.getLongitude()).isEqualTo(longitude)
        );
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

    @DisplayName("프로필 사진 업데이트 요청에 성공하면 프로필 사진 접근 가능 식별값과 200을 반환한다.")
    @Test
    void updateMemberProfileImage_OK() {
        String token = getToken();
        File testImgFile = generatetestImgFile();

        ExtractableResponse<Response> response =
                httpPutWithAuthorizationAndImgFile(MEMBER_PROFILE_IMAGE_URI, token, testImgFile);

        String profileImageUrl = response.jsonPath()
                .getObject(".", ProfileImageUpdateResponse.class)
                .getProfileImageUrl();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(profileImageUrl).contains(TEST_IMAGE_FILE_NAME)
        );
    }

    @DisplayName("프로필 사진 업데이트 요청에 실패하면 500을 반환한다.")
    @Test
    void updateMemberProfileImage_InternalServerError() {
        String token = getToken();
        File testImgFile = generatetestImgFile();
        mockAmazonS3Exception();

        ExtractableResponse<Response> response =
                httpPutWithAuthorizationAndImgFile(MEMBER_PROFILE_IMAGE_URI, token, testImgFile);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void mockAmazonS3Exception() {
        Mockito.doThrow(AmazonClientException.class)
                .when(amazonS3)
                .putObject(any(), any(), any(), any());
    }

    private String getNickname(String token) {
        return httpGetWithAuthorization(MEMBER_INFO_URI, token)
                .jsonPath()
                .getObject(".", MemberInfoResponse.class)
                .getNickname();
    }
}
