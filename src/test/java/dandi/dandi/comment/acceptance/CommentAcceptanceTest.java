package dandi.dandi.comment.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.comment.application.port.in.CommentRegisterCommand;
import dandi.dandi.common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CommentAcceptanceTest extends AcceptanceTest {

    @DisplayName("댓글 작성 요청에 성공하면 204를 응답한다.")
    @Test
    void registerComment_NoContent() {
        String token = getToken();
        Long postId = registerPost(token);
        CommentRegisterCommand commentRegisterCommand = new CommentRegisterCommand("댓글 내용");

        ExtractableResponse<Response> response =
                httpPostWithAuthorization("/posts/" + postId + "/comments", commentRegisterCommand, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 글에 대한 댓글 작성 요청에 대해 404를 응답한다.")
    @Test
    void registerComment_NotFound() {
        String token = getToken();
        Long notFoundPostId = 1L;
        CommentRegisterCommand commentRegisterCommand = new CommentRegisterCommand("댓글 내용");

        ExtractableResponse<Response> response =
                httpPostWithAuthorization("/posts/" + notFoundPostId + "/comments", commentRegisterCommand, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
