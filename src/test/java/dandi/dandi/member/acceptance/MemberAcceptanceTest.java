package dandi.dandi.member.acceptance;

import static dandi.dandi.comment.CommentFixture.COMMENT_REGISTER_COMMAND;
import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPatchWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPost;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPutWithAuthorizationAndImgFile;
import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static dandi.dandi.common.RequestURI.FEED_REQUEST_URI;
import static dandi.dandi.common.RequestURI.LOGIN_REQUEST_URI;
import static dandi.dandi.common.RequestURI.MEMBER_BLOCK_REQUEST_URI;
import static dandi.dandi.common.RequestURI.MEMBER_DEFAULT_PROFILE_IMAGE;
import static dandi.dandi.common.RequestURI.MEMBER_INFO_URI;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_DUPLICATION_CHECK_URI;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_LOCATION;
import static dandi.dandi.common.RequestURI.MEMBER_NICKNAME_URI;
import static dandi.dandi.common.RequestURI.MEMBER_PROFILE_IMAGE_URI;
import static dandi.dandi.member.MemberTestFixture.DISTRICT_VALUE;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.utils.TestImageUtils.IMAGE_ACCESS_URL;
import static dandi.dandi.utils.TestImageUtils.TEST_IMAGE_FILE_NAME;
import static dandi.dandi.utils.TestImageUtils.generateTestImgFile;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.adapter.in.web.dto.LoginRequest;
import dandi.dandi.auth.application.port.in.TokenResponse;
import dandi.dandi.comment.application.port.in.CommentResponse;
import dandi.dandi.comment.application.port.in.CommentResponses;
import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.member.adapter.in.web.dto.in.LocationUpdateRequest;
import dandi.dandi.member.adapter.in.web.dto.in.NicknameUpdateRequest;
import dandi.dandi.member.application.port.in.MemberBlockCommand;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.port.in.ProfileImageUpdateResponse;
import dandi.dandi.post.application.port.in.FeedResponse;
import dandi.dandi.post.application.port.in.PostResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("기본 프로필 이미지 회원 정보 요청에 대해 200을 반환한다.")
    @Test
    void getMemberInfo() {
        String token = getToken();
        double initialLatitude = 0.0;
        double initialLongitude = 0.0;
        registerPost(token);
        registerPost(token);

        ExtractableResponse<Response> response = httpGetWithAuthorization(MEMBER_INFO_URI, token);

        MemberInfoResponse memberInfoResponse = response.jsonPath()
                .getObject(".", MemberInfoResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberInfoResponse.getNickname()).isEqualTo(memberInfoResponse.getNickname()),
                () -> assertThat(memberInfoResponse.getPostCount()).isEqualTo(2),
                () -> assertThat(memberInfoResponse.getLatitude()).isEqualTo(initialLatitude),
                () -> assertThat(memberInfoResponse.getLongitude()).isEqualTo(initialLongitude),
                () -> assertThat(memberInfoResponse.getProfileImageUrl())
                        .isEqualTo(IMAGE_ACCESS_URL + MEMBER_DEFAULT_PROFILE_IMAGE)
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

    @DisplayName("이미 존재하는 닉네임으로 닉네임을 변경을 하려는 요청에 대해 400을 반환한다.")
    @Test
    void updateMemberNickname_DuplicatedNickname() {
        String token = getToken();
        String anotherMemberAppleIdToken = "anotherMemberAppleIdToken";
        String anotherMemberAuthId = "anotherMemberAuthId";
        when(oAuthClientPort.getOAuthMemberId(anotherMemberAppleIdToken))
                .thenReturn(OAUTH_ID);
        String anotherMemberAccessToken = httpPost(new LoginRequest(anotherMemberAuthId, PUSH_NOTIFICATION_TOKEN),
                LOGIN_REQUEST_URI)
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
        String anotherMemberToken = getAnotherMemberToken();

        ExtractableResponse<Response> response = httpGetWithAuthorization(
                MEMBER_NICKNAME_DUPLICATION_CHECK_URI + "?nickname=" + nickname, anotherMemberToken);

        boolean duplicated = response.jsonPath()
                .getObject(".", NicknameDuplicationCheckResponse.class)
                .isDuplicated();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(duplicated).isEqualTo(expected)
        );
    }

    @DisplayName("자신의 닉네임에 대한 닉네임 중복확인 요청에 대해 200과 body에 false와 반환한다.")
    @Test
    void checkNicknameDuplication_MyNickname() {
        String token = getToken();
        String nickname = "nickname123";
        httpPatchWithAuthorization(MEMBER_NICKNAME_URI, new NicknameUpdateRequest(nickname), token);

        ExtractableResponse<Response> response =
                httpGetWithAuthorization(MEMBER_NICKNAME_DUPLICATION_CHECK_URI + "?nickname=" + nickname, token);

        boolean duplicated = response.jsonPath()
                .getObject(".", NicknameDuplicationCheckResponse.class)
                .isDuplicated();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(duplicated).isFalse()
        );
    }

    @DisplayName("사용자 위치 정보 변경에 성공하면 204를 반환한다.")
    @Test
    void updateMemberLocation_NoContent() {
        String token = getToken();
        double latitude = 1.0;
        double longitude = 2.0;
        LocationUpdateRequest locationUpdateRequest = new LocationUpdateRequest(latitude, longitude, DISTRICT_VALUE);

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
        LocationUpdateRequest invalidLocationUpdateRequest = new LocationUpdateRequest(invalidLatitude, -2.0,
                DISTRICT_VALUE);

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
        File testImgFile = generateTestImgFile();

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
        File testImgFile = generateTestImgFile();
        mockAmazonS3Exception();

        ExtractableResponse<Response> response =
                httpPutWithAuthorizationAndImgFile(MEMBER_PROFILE_IMAGE_URI, token, testImgFile);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("다른 사용자 차단 요청에 성공하면 201을 응답하고 차단한 사용자의 게시글과 댓글들은 더이상 노출되지 않는다.")
    @Test
    void blockMember_Created() {
        String token = getToken();
        String anotherToken = getAnotherMemberToken();
        Long anotherMemberId = 2L;
        registerPost(anotherToken);
        Long postId = registerPost(token);
        httpPostWithAuthorization("/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, anotherToken);

        ExtractableResponse<Response> response =
                httpPostWithAuthorization(MEMBER_BLOCK_REQUEST_URI, new MemberBlockCommand(anotherMemberId), token);

        String feedQueryString = "?min=20.0&max=30.0&page=0&size=10&sort=createdAt,DESC";
        List<PostResponse> feedPostsAfterBlock = httpGetWithAuthorization(
                FEED_REQUEST_URI + feedQueryString, token)
                .jsonPath()
                .getObject(".", FeedResponse.class)
                .getPosts();
        String commentsQueryString = "?page=0&size=10&sort=createdAt,DESC";
        List<CommentResponse> commentsAfterBlock = httpGetWithAuthorization(
                "/posts/" + postId + "/comments" + commentsQueryString, token)
                .jsonPath()
                .getObject(".", CommentResponses.class)
                .getComments();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(feedPostsAfterBlock).hasSize(1),
                () -> assertThat(commentsAfterBlock).isEmpty()
        );
    }

    @DisplayName("다른 사용자 차단 요청에 성공하면 201을 응답하고 차단 당한 사용자는 차단을 한 사용자의 게시글과 댓글을 조회할 수 없다.")
    @Test
    void blockMember_BlockingMemberComment() {
        String token = getToken();
        String anotherToken = getAnotherMemberToken();
        Long anotherMemberId = 2L;
        Long postId = registerPost(token);
        httpPostWithAuthorization("/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, token);

        ExtractableResponse<Response> response = httpPostWithAuthorization(
                MEMBER_BLOCK_REQUEST_URI, new MemberBlockCommand(anotherMemberId), token);

        String feedQueryString = "?min=20.0&max=30.0&page=0&size=10&sort=createdAt,DESC";
        List<PostResponse> feedPostsAfterBlock = httpGetWithAuthorization(
                FEED_REQUEST_URI + feedQueryString, anotherToken)
                .jsonPath()
                .getObject(".", FeedResponse.class)
                .getPosts();
        String commentsQueryString = "?page=0&size=10&sort=createdAt,DESC";
        List<CommentResponse> commentsAfterBlock = httpGetWithAuthorization(
                "/posts/" + postId + "/comments" + commentsQueryString, anotherToken)
                .jsonPath()
                .getObject(".", CommentResponses.class)
                .getComments();
        assertAll(
                () -> AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(feedPostsAfterBlock).isEmpty(),
                () -> assertThat(commentsAfterBlock).isEmpty()
        );
    }

    @DisplayName("존재하지 않는 사용자 차단 요청에 대해 404를 응답한다.")
    @Test
    void blockMember_NotFound() {
        String token = getToken();
        Long notFoundMemberId = 2L;

        ExtractableResponse<Response> response =
                httpPostWithAuthorization(MEMBER_BLOCK_REQUEST_URI, new MemberBlockCommand(notFoundMemberId), token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("이미 차단한 사용자 차단 요청에 대해 400을 응답한다.")
    @Test
    void blockMember_BadRequest() {
        String token = getToken();
        signUpAnotherMember();
        Long anotherMemberId = 2L;
        httpPostWithAuthorization(MEMBER_BLOCK_REQUEST_URI, new MemberBlockCommand(anotherMemberId), token);

        ExtractableResponse<Response> response =
                httpPostWithAuthorization(MEMBER_BLOCK_REQUEST_URI, new MemberBlockCommand(anotherMemberId), token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void signUpAnotherMember() {
        getAnotherMemberToken();
    }

    private String getNickname(String token) {
        return httpGetWithAuthorization(MEMBER_INFO_URI, token)
                .jsonPath()
                .getObject(".", MemberInfoResponse.class)
                .getNickname();
    }
}
