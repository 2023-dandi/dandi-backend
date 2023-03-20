package dandi.dandi.postlike.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPatchWithAuthorization;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.post.application.port.in.PostDetailResponse;
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
        boolean likedBeforePostLikeRequest = httpGetWithAuthorization("/posts/" + postId, token)
                .jsonPath()
                .getObject(".", PostDetailResponse.class)
                .isLiked();

        ExtractableResponse<Response> response = httpPatchWithAuthorization(
                "/posts/" + postId + "/likes", token);

        boolean likedAfterPostLikeRequest = httpGetWithAuthorization("/posts/" + postId, token)
                .jsonPath()
                .getObject(".", PostDetailResponse.class)
                .isLiked();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(likedBeforePostLikeRequest).isFalse(),
                () -> assertThat(likedAfterPostLikeRequest).isTrue()
        );
    }

    @DisplayName("자신이 좋아요를 누른 게시글에 좋아요를 취소할 수 있다.")
    @Test
    void reverseLikes_Cancel_NoContent() {
        String token = getToken();
        Long postId = registerPost(token);
        httpPatchWithAuthorization("/posts/" + postId + "/likes", token);
        boolean likedBeforePostLikeRequest = httpGetWithAuthorization("/posts/" + postId, token)
                .jsonPath()
                .getObject(".", PostDetailResponse.class)
                .isLiked();

        ExtractableResponse<Response> response = httpPatchWithAuthorization(
                "/posts/" + postId + "/likes", token);

        boolean likedAfterPostLikeRequest = httpGetWithAuthorization("/posts/" + postId, token)
                .jsonPath()
                .getObject(".", PostDetailResponse.class)
                .isLiked();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(likedBeforePostLikeRequest).isTrue(),
                () -> assertThat(likedAfterPostLikeRequest).isFalse()
        );
    }
}
