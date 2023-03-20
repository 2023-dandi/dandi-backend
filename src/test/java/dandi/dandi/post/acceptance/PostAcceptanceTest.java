package dandi.dandi.post.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpGet;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorizationAndImgFile;
import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static dandi.dandi.common.RequestURI.POST_DETAILS_REQUEST_URI;
import static dandi.dandi.common.RequestURI.POST_IMAGE_REGISTER_REQUEST_URI;
import static dandi.dandi.common.RequestURI.POST_REGISTER_REQUEST_URI;
import static dandi.dandi.post.PostFixture.ADDITIONAL_OUTFIT_FEELING_INDICES;
import static dandi.dandi.post.PostFixture.MAX_TEMPERATURE;
import static dandi.dandi.post.PostFixture.MIN_TEMPERATURE;
import static dandi.dandi.post.PostFixture.OUTFIT_FEELING_INDEX;
import static dandi.dandi.post.PostFixture.POST_IMAGE_URL;
import static dandi.dandi.utils.image.TestImageUtils.generatetestImgFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;

import com.amazonaws.AmazonClientException;
import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostImageRegisterResponse;
import dandi.dandi.post.web.in.OutfitFeelingRequest;
import dandi.dandi.post.web.in.PostRegisterRequest;
import dandi.dandi.post.web.in.TemperatureRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class PostAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시글 등록에 성공하면 201과 게시글에 접근할 수 있는 URI를 Location 헤더에 반환한다.")
    @Test
    void registerPost_Created() {
        String token = getToken();
        PostRegisterRequest postRegisterRequest = new PostRegisterRequest(POST_IMAGE_URL,
                new TemperatureRequest(MIN_TEMPERATURE, MAX_TEMPERATURE),
                new OutfitFeelingRequest(OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES));

        ExtractableResponse<Response> response =
                httpPostWithAuthorization(POST_REGISTER_REQUEST_URI, postRegisterRequest, token);

        String locationHeader = response.header(HttpHeaders.LOCATION);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(locationHeader).contains("/posts")
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
        File testImgFile = generatetestImgFile();

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
        File testImgFile = generatetestImgFile();

        ExtractableResponse<Response> response = httpPostWithAuthorizationAndImgFile(
                POST_IMAGE_REGISTER_REQUEST_URI, token, testImgFile, "postImage");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("게시글 상세 조회 요청에 성공하면 200과 게시글 상세 정보를 반환한다.")
    @Test
    void getPostDetails() {
        String token = getToken();
        Long postId = registerPost(token);

        ExtractableResponse<Response> response = httpGet(POST_DETAILS_REQUEST_URI + "/" + postId);

        PostDetailResponse postDetailResponse = response.jsonPath()
                .getObject(".", PostDetailResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postDetailResponse.getPostImageUrl()).isNotNull(),
                () -> assertThat(postDetailResponse.getWriterNickname()).isNotNull(),
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
        ExtractableResponse<Response> response = httpGet(POST_DETAILS_REQUEST_URI + "/" + 1L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public Long registerPost(String token) {
        PostRegisterRequest postRegisterRequest = new PostRegisterRequest(POST_IMAGE_URL,
                new TemperatureRequest(MIN_TEMPERATURE, MAX_TEMPERATURE),
                new OutfitFeelingRequest(OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES));
        String locationHeader = httpPostWithAuthorization(POST_REGISTER_REQUEST_URI, postRegisterRequest, token)
                .header(HttpHeaders.LOCATION);
        String postId = locationHeader.split("/posts/")[1];
        return Long.parseLong(postId);
    }

    private void mockAmazonS3Exception() {
        Mockito.doThrow(AmazonClientException.class)
                .when(amazonS3)
                .putObject(any(), any(), any(), any());
    }
}
