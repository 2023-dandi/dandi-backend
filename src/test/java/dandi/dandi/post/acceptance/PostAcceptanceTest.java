package dandi.dandi.post.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpDeleteWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorizationAndPagination;
import static dandi.dandi.common.HttpMethodFixture.httpPatchWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorizationAndImgFile;
import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static dandi.dandi.common.RequestURI.FEED_REQUEST_URI;
import static dandi.dandi.common.RequestURI.LIKED_POST_REQUEST_URI;
import static dandi.dandi.common.RequestURI.MY_POST_BY_TEMPERATURE_REQUEST_URI;
import static dandi.dandi.common.RequestURI.MY_POST_REQUEST_URI;
import static dandi.dandi.common.RequestURI.POST_DETAILS_REQUEST_URI;
import static dandi.dandi.common.RequestURI.POST_IMAGE_REGISTER_REQUEST_URI;
import static dandi.dandi.common.RequestURI.POST_REGISTER_REQUEST_URI;
import static dandi.dandi.post.PostFixture.ADDITIONAL_OUTFIT_FEELING_INDICES;
import static dandi.dandi.post.PostFixture.MAX_TEMPERATURE;
import static dandi.dandi.post.PostFixture.MIN_TEMPERATURE;
import static dandi.dandi.post.PostFixture.OUTFIT_FEELING_INDEX;
import static dandi.dandi.post.PostFixture.POST_IMAGE_FULL_URL;
import static dandi.dandi.post.PostFixture.POST_IMAGE_URL;
import static dandi.dandi.utils.TestImageUtils.generateTestImgFile;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.post.adapter.in.web.dto.OutfitFeelingRequest;
import dandi.dandi.post.adapter.in.web.dto.PostRegisterRequest;
import dandi.dandi.post.adapter.in.web.dto.TemperatureRequest;
import dandi.dandi.post.application.port.in.FeedResponse;
import dandi.dandi.post.application.port.in.LikedPostResponse;
import dandi.dandi.post.application.port.in.LikedPostResponses;
import dandi.dandi.post.application.port.in.MyPostResponse;
import dandi.dandi.post.application.port.in.MyPostResponses;
import dandi.dandi.post.application.port.in.MyPostsByTemperatureResponses;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostImageRegisterResponse;
import dandi.dandi.post.application.port.in.PostRegisterResponse;
import dandi.dandi.post.application.port.in.PostResponse;
import dandi.dandi.post.application.port.in.PostWriterResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;

class PostAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시글 등록에 성공하면 201과 게시글 ID를 반환한다.")
    @Test
    void registerPost_Created() {
        String token = getToken();
        PostRegisterRequest postRegisterRequest = new PostRegisterRequest(POST_IMAGE_FULL_URL,
                new TemperatureRequest(MIN_TEMPERATURE, MAX_TEMPERATURE),
                new OutfitFeelingRequest(OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES));

        ExtractableResponse<Response> response =
                httpPostWithAuthorization(POST_REGISTER_REQUEST_URI, postRegisterRequest, token);

        Long postId = response.jsonPath()
                .getObject(".", PostRegisterResponse.class)
                .getPostId();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(postId).isNotNull()
        );
    }

    @DisplayName("1 ~ 5 범위가 아닌 착장 느낌을 포함한 게시글 등록 요청에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1, 5})
    void registerPost_BadRequest_InvalidRangeOutfitFeelingIndex(Long outfitFeelingIndex) {
        String token = getToken();
        PostRegisterRequest invalidRangeOutfitFeelingIndexPostRegisterRequest = new PostRegisterRequest(
                POST_IMAGE_URL, new TemperatureRequest(MIN_TEMPERATURE, MAX_TEMPERATURE),
                new OutfitFeelingRequest(outfitFeelingIndex, ADDITIONAL_OUTFIT_FEELING_INDICES));

        ExtractableResponse<Response> response = httpPostWithAuthorization(
                POST_REGISTER_REQUEST_URI, invalidRangeOutfitFeelingIndexPostRegisterRequest, token);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(extractExceptionMessage(response)).isNotNull()
        );
    }

    @DisplayName("게시글 사진 등록에 성공하면 201과 게시글 사진 URL을 응답한다.")
    @Test
    void registerPostImage_Created() {
        String token = getToken();
        File testImgFile = generateTestImgFile();

        ExtractableResponse<Response> response = httpPostWithAuthorizationAndImgFile(
                POST_IMAGE_REGISTER_REQUEST_URI, token, testImgFile, "postImage");

        String locationHeader = response.jsonPath()
                .getObject(".", PostImageRegisterResponse.class)
                .getPostImageUrl();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(locationHeader).isNotNull()
        );
    }

    @DisplayName("게시글 사진 등록에 실패하면 500을 응답한다.")
    @Test
    void registerPostImage_InternalServerError() {
        String token = getToken();
        mockAmazonS3Exception();
        File testImgFile = generateTestImgFile();

        ExtractableResponse<Response> response = httpPostWithAuthorizationAndImgFile(
                POST_IMAGE_REGISTER_REQUEST_URI, token, testImgFile, "postImage");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("게시글 상세 조회 요청에 성공하면 200과 게시글 상세 정보를 반환한다.")
    @Test
    void getPostDetails() {
        String token = getToken();
        Long postId = registerPost(token);

        ExtractableResponse<Response> response =
                httpGetWithAuthorization(POST_DETAILS_REQUEST_URI + "/" + postId, token);

        PostDetailResponse postDetailResponse = response.jsonPath()
                .getObject(".", PostDetailResponse.class);
        PostWriterResponse postWriterResponse = postDetailResponse.getWriter();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postDetailResponse.getPostImageUrl()).isNotNull(),
                () -> assertThat(postWriterResponse.getId()).isNotNull(),
                () -> assertThat(postWriterResponse.getNickname()).isNotNull(),
                () -> assertThat(postWriterResponse.getProfileImageUrl()).isNotNull(),
                () -> assertThat(postDetailResponse.isMine()).isTrue(),
                () -> assertThat(postDetailResponse.isLiked()).isFalse(),
                () -> assertThat(postDetailResponse.getOutfitFeelings().getFeelingIndex())
                        .isEqualTo(OUTFIT_FEELING_INDEX),
                () -> assertThat(postDetailResponse.getOutfitFeelings().getAdditionalFeelingIndices())
                        .isEqualTo(ADDITIONAL_OUTFIT_FEELING_INDICES),
                () -> assertThat(postDetailResponse.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("존재하지 않는 게시글 상세 조회 요청에 대해 404를 반환한다.")
    @Test
    void getPostDetails_NotFount() {
        String token = getToken();
        ExtractableResponse<Response> response =
                httpGetWithAuthorization(POST_DETAILS_REQUEST_URI + "/" + 1L, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("게시글을 삭제할 수 있다.")
    @Test
    void deletePost() {
        String token = getToken();
        Long postId = registerPost(token);

        ExtractableResponse<Response> response = httpDeleteWithAuthorization("/posts/" + postId, token);

        ExtractableResponse<Response> postDetailResponseAfterPostDeletion =
                httpGetWithAuthorization("/posts/" + postId, token);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(postDetailResponseAfterPostDeletion.statusCode())
                        .isEqualTo(HttpStatus.NOT_FOUND.value())
        );
    }

    @DisplayName("내가 작성한 게시글 목록 요청(페이지네이션 o)에 200과 게시글들의 id와 image url을 응답한다.")
    @ParameterizedTest
    @CsvSource({"3, false", "4, true"})
    void getMyPostIdsAndPostImageUrls_SpecifiedPagination(int size, boolean expectedLastPage) {
        String token = getToken();
        registerPost(token);
        registerPost(token);
        registerPost(token);
        registerPost(token);

        ExtractableResponse<Response> response = httpGetWithAuthorizationAndPagination(
                MY_POST_REQUEST_URI, token, size, 0, "createdAt", Direction.DESC);

        MyPostResponses myPostsPageableResponses = response.jsonPath()
                .getObject(".", MyPostResponses.class);
        List<MyPostResponse> posts = myPostsPageableResponses.getPosts();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(myPostsPageableResponses.isLastPage()).isEqualTo(expectedLastPage),
                () -> assertThat(posts).hasSize(size),
                () -> assertThat(posts.get(0).getId()).isEqualTo(4L),
                () -> assertThat(posts.get(1).getId()).isEqualTo(3L),
                () -> assertThat(posts.get(0).getPostImageUrl()).isNotNull(),
                () -> assertThat(posts.get(1).getPostImageUrl()).isNotNull()
        );
    }

    @DisplayName("내가 작성한 게시글 목록(페이지네이션 x) 요청에 200과 500개 이하의 게시글들의 id와 image url을 생성 내림차순으로 응답한다.")
    @Test
    void getMyPostIdsAndPostImageUrls_UnspecifiedPagination() {
        String token = getToken();
        registerPost(token);
        registerPost(token);
        registerPost(token);
        registerPost(token);

        ExtractableResponse<Response> response = httpGetWithAuthorization(MY_POST_REQUEST_URI, token);

        MyPostResponses myPostsPageableResponses = response.jsonPath()
                .getObject(".", MyPostResponses.class);
        List<MyPostResponse> posts = myPostsPageableResponses.getPosts();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(myPostsPageableResponses.isLastPage()).isTrue(),
                () -> assertThat(posts).hasSize(4),
                () -> assertThat(posts.get(0).getId()).isEqualTo(4L),
                () -> assertThat(posts.get(1).getId()).isEqualTo(3L),
                () -> assertThat(posts.get(0).getPostImageUrl()).isNotNull(),
                () -> assertThat(posts.get(1).getPostImageUrl()).isNotNull()
        );
    }

    @DisplayName("최저 최고 기온의 +- 3도 게시글 요청에 게시글들과 200을 응답한다.")
    @Test
    void getFeedsByTemperature() {
        String token = getToken();
        String anotherMemberToken = getAnotherMemberToken();
        registerPostWithTemperature(token, 10.0, 20.0);
        registerPostWithTemperature(token, 11.0, 20.0);
        registerPostWithTemperature(anotherMemberToken, 11.0, 20.0);
        registerPostWithTemperature(token, 12.0, 20.0);
        registerPostWithTemperature(token, 20.0, 20.0);
        String feedQueryString = "?min=11.0&max=20.0&page=0&size=10&sort=createdAt,DESC";

        ExtractableResponse<Response> response = httpGetWithAuthorization(FEED_REQUEST_URI + feedQueryString, token);

        FeedResponse feedResponse = response.jsonPath()
                .getObject(".", FeedResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(feedResponse.isLastPage()).isTrue(),
                () -> assertThat(feedResponse.getPosts()).hasSize(4)
        );
    }

    @DisplayName("최저 최고 기온의 +- 3도인 본인의 게시글 요청에 게시글들과 200을 응답한다.")
    @Test
    void getMyPostsByTemperature() {
        String token = getToken();
        String anotherMemberToken = getAnotherMemberToken();
        registerPostWithTemperature(token, 12.0, 20.0);
        registerPostWithTemperature(token, 10.0, 20.0);
        registerPostWithTemperature(anotherMemberToken, 10.0, 20.0);
        registerPostWithTemperature(token, 20.0, 20.0);

        String queryString = "?min=10&max=20&page=0&size=10&sort=createdAt,DESC";
        ExtractableResponse<Response> response =
                httpGetWithAuthorization(MY_POST_BY_TEMPERATURE_REQUEST_URI + queryString, token);

        MyPostsByTemperatureResponses myPostsByTemperatureResponses = response.jsonPath()
                .getObject(".", MyPostsByTemperatureResponses.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(myPostsByTemperatureResponses.isLastPage()).isTrue(),
                () -> assertThat(myPostsByTemperatureResponses.getWriter()).isNotNull(),
                () -> assertThat(myPostsByTemperatureResponses.getPosts()).hasSize(2)
        );
    }

    @DisplayName("게시글 신고에 성공하면 201을 응답하고 해당 게시글은 더이상 신고자에게 노출되지 않는다.")
    @Test
    void reportPost_Created() {
        String token = getToken();
        Long postId = registerPost(token);
        String anotherToken = getAnotherMemberToken();

        ExtractableResponse<Response> response =
                httpPostWithAuthorization("/posts/" + postId + "/reports", anotherToken);

        String feedQueryString = "?min=11.0&max=20.0&page=0&size=10&sort=createdAt,DESC";
        List<PostResponse> feedPostsAfterReport = httpGetWithAuthorization(
                FEED_REQUEST_URI + feedQueryString, anotherToken)
                .jsonPath()
                .getObject(".", FeedResponse.class)
                .getPosts();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(feedPostsAfterReport).isEmpty()
        );
    }

    @DisplayName("존재하지 않는 게시글 신고 요청에 대해 404를 응답한다.")
    @Test
    void reportPost_NotFound() {
        String token = getToken();

        ExtractableResponse<Response> response =
                httpPostWithAuthorization("/posts/1/reports", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("이미 신고한 게시글에 대한 신고 요청에 대해 400을 응답한다.")
    @Test
    void reportPost_BadRequest() {
        String token = getToken();
        Long postId = registerPost(token);
        String anotherToken = getAnotherMemberToken();
        httpPostWithAuthorization("/posts/" + postId + "/reports", anotherToken);

        ExtractableResponse<Response> response =
                httpPostWithAuthorization("/posts/" + postId + "/reports", anotherToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("좋아요 누른 게시글 요청에 대해 200과 게시글들을 응답한다.")
    @Test
    void getLikedPosts() {
        String token = getToken();
        Long firstPostId = registerPost(token);
        Long secondPostId = registerPost(token);
        registerPost(token);
        String anotherToken = getAnotherMemberToken();
        httpPatchWithAuthorization("/posts/" + secondPostId + "/likes", anotherToken);
        httpPatchWithAuthorization("/posts/" + firstPostId + "/likes", anotherToken);

        ExtractableResponse<Response> response = httpGetWithAuthorization(LIKED_POST_REQUEST_URI, anotherToken);

        List<LikedPostResponse> posts = response.jsonPath()
                .getObject(".", LikedPostResponses.class)
                .getPosts();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(posts).hasSize(2),
                () -> assertThat(posts.get(0).getId()).isEqualTo(2L),
                () -> assertThat(posts.get(1).getId()).isEqualTo(1L)
        );
    }

    private void registerPostWithTemperature(String token, Double minTemperature, Double maxTemperature) {
        PostRegisterRequest postRegisterRequest = new PostRegisterRequest(POST_IMAGE_FULL_URL,
                new TemperatureRequest(minTemperature, maxTemperature),
                new OutfitFeelingRequest(OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES));
        httpPostWithAuthorization(POST_REGISTER_REQUEST_URI, postRegisterRequest, token)
                .jsonPath()
                .getObject(".", PostRegisterResponse.class);
    }
}
