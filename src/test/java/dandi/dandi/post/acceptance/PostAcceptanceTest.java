package dandi.dandi.post.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
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
}
