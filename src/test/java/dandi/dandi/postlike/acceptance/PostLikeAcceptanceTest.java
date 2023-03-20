package dandi.dandi.postlike.acceptance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.common.HttpMethodFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class PostLikeAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시글에 좋아요를 등록할 수 있다.")
    @Test
    void reverseLikes_Register_NoContent() {
        String token = getToken();
        Long postId = registerPost(token);

        ExtractableResponse<Response> response = HttpMethodFixture.httpPatchWithAuthorization(
                "/posts/" + postId + "/likes", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("자신이 좋아요를 누른 게시글에 좋아요를 취소할 수 있다.")
    @Test
    void reverseLikes_Deletion_NoContent() {
        String token = getToken();
        Long postId = registerPost(token);
        HttpMethodFixture.httpPatchWithAuthorization("/posts/" + postId + "/likes", token);

        ExtractableResponse<Response> response = HttpMethodFixture.httpPatchWithAuthorization(
                "/posts/" + postId + "/likes", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
