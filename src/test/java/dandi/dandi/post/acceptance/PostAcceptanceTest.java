package dandi.dandi.post.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static dandi.dandi.common.RequestURI.POST_REGISTER_REQUEST_URI;
import static dandi.dandi.post.PostFixture.ADDITIONAL_OUTFIT_FEELING_INDICES;
import static dandi.dandi.post.PostFixture.MAX_TEMPERATURE;
import static dandi.dandi.post.PostFixture.MIN_TEMPERATURE;
import static dandi.dandi.post.PostFixture.OUTFIT_FEELING_INDEX;
import static dandi.dandi.post.PostFixture.POST_IMAGE_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.post.web.in.FeelingRequest;
import dandi.dandi.post.web.in.PostRegisterRequest;
import dandi.dandi.post.web.in.TemperatureRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class PostAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시글 등록에 성공하면 201과 게시글에 접근할 수 있는 URI를 Location 헤더에 반환한다.")
    @Test
    void registerPost() {
        String token = getToken();
        PostRegisterRequest postRegisterRequest = new PostRegisterRequest(POST_IMAGE_URL,
                new TemperatureRequest(MIN_TEMPERATURE, MAX_TEMPERATURE),
                new FeelingRequest(OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES));

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
    @ValueSource(longs = {0, 6})
    void registerPost_BadRequest_InvalidRangeOutfitFeelingIndex(Long outfitFeelingIndex) {
        String token = getToken();
        PostRegisterRequest invalidRangeOutfitFeelingIndexPostRegisterRequest = new PostRegisterRequest(
                POST_IMAGE_URL, new TemperatureRequest(MIN_TEMPERATURE, MAX_TEMPERATURE),
                new FeelingRequest(outfitFeelingIndex, ADDITIONAL_OUTFIT_FEELING_INDICES));

        ExtractableResponse<Response> response = httpPostWithAuthorization(
                POST_REGISTER_REQUEST_URI, invalidRangeOutfitFeelingIndexPostRegisterRequest, token);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(extractExceptionMessage(response)).isNotNull()
        );
    }
}
