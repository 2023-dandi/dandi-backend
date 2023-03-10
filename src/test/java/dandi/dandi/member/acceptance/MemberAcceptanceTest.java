package dandi.dandi.member.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpGet;
import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPatchWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPost;
import static dandi.dandi.common.HttpMethodFixture.httpPutWithAuthorizationAndImgFile;
import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static dandi.dandi.common.RequestURI.LOGIN_REQUEST_URI;
import static dandi.dandi.common.RequestURI.MEMBER_INFO_URI;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_DUPLICATION_CHECK_URI;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_LOCATION;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_URI;
import static dandi.dandi.common.RequestURI.MEMBER_PROFILE_IMAGE_URI;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.utils.image.TestImageUtils.TEST_IMAGE_FILE_NAME;
import static dandi.dandi.utils.image.TestImageUtils.generatetestImgFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.amazonaws.AmazonClientException;
import dandi.dandi.auth.application.port.in.TokenResponse;
import dandi.dandi.auth.web.in.LoginRequest;
import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.port.in.ProfileImageUpdateResponse;
import dandi.dandi.member.web.dto.in.LocationUpdateRequest;
import dandi.dandi.member.web.dto.in.NicknameUpdateRequest;
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

    @DisplayName("?????? ????????? ????????? ?????? ?????? ????????? ?????? 200??? ????????????.(????????? ????????? ????????? URL ??????)")
    @Test
    void getMemberNickname_CustomProfileImageMember() {
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
                () -> assertThat(memberInfoResponse.getProfileImageUrl()).isNull()
        );
    }

    @DisplayName("????????? ????????? ????????? ?????? ?????? ????????? ?????? 200??? ????????????.(????????? ???????????? null ??????)")
    @Test
    void getMemberNickname_InitialProfileImageMember() {
        String token = getToken();
        double initialLatitude = 0.0;
        double initialLongitude = 0.0;
        httpPutWithAuthorizationAndImgFile(MEMBER_PROFILE_IMAGE_URI, token, generatetestImgFile());

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

    @DisplayName("?????? ????????? ?????? ????????? ???????????? 204??? ????????????.")
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

    @DisplayName("?????? ???????????? ??????????????? ???????????? ????????? ????????? ????????? ?????? 400??? ????????????.")
    @Test
    void updateMemberNickname_DuplicatedNickname() {
        String token = getToken();
        String anotherMemberAppleIdToken = "anotherMemberAppleIdToken";
        String anotherMemberAuthId = "anotherMemberAuthId";
        when(oAuthClientPort.getOAuthMemberId(anotherMemberAppleIdToken))
                .thenReturn(OAUTH_ID);
        String anotherMemberAccessToken = httpPost(new LoginRequest(anotherMemberAuthId), LOGIN_REQUEST_URI)
                .jsonPath()
                .getObject(".", TokenResponse.class)
                .getAccessToken();
        String newNickname = "newNickname";
        httpPatchWithAuthorization(MEMBER_NICKNAME_URI, new NicknameUpdateRequest(newNickname),
                anotherMemberAccessToken);

        ExtractableResponse<Response> response =
                httpPatchWithAuthorization(MEMBER_NICKNAME_URI, new NicknameUpdateRequest(newNickname), token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("????????? ???????????? ?????? ????????? ?????? ????????? ?????? 400??? ????????????.")
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

    @DisplayName("????????? ?????? ?????? ????????? ?????? ??????????????? 200??? ????????????.")
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

    @DisplayName("????????? ?????? ?????? ????????? ???????????? 204??? ????????????.")
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

    @DisplayName("????????? ????????? ????????? ?????? ?????? ????????? ?????? 400??? ????????????.")
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

    @DisplayName("????????? ?????? ???????????? ????????? ???????????? ????????? ?????? ?????? ?????? ???????????? 200??? ????????????.")
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

    @DisplayName("????????? ?????? ???????????? ????????? ???????????? 500??? ????????????.")
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
