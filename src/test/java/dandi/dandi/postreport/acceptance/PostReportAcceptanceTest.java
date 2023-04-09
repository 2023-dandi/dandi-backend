package dandi.dandi.postreport.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class PostReportAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시글 신고에 성공하면 201을 응답한다.")
    @Test
    void reportPost_Created() {
        String token = getToken();
        Long postId = registerPost(token);
        String anotherToken = getAnotherMemberToken();

        ExtractableResponse<Response> response =
                httpPostWithAuthorization("/posts/" + postId + "/reports", anotherToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
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
}
