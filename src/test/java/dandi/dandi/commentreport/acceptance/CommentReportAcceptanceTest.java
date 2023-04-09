package dandi.dandi.commentreport.acceptance;

import static dandi.dandi.comment.CommentFixture.COMMENT_REGISTER_COMMAND;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CommentReportAcceptanceTest extends AcceptanceTest {

    @DisplayName("댓글 신고 요청에 성공하면 201을 응답한다.")
    @Test
    void reportComment_Created() {
        String token = getToken();
        Long postId = registerPost(token);
        String anotherToken = getAnotherMemberToken();
        httpPostWithAuthorization("/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, anotherToken);

        ExtractableResponse<Response> response = httpPostWithAuthorization("/comments/1/reports", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("이미 신고한 댓글 신고 요청에 대해 400을 응답한다.")
    @Test
    void reportComment_BadRequest() {
        String token = getToken();
        Long postId = registerPost(token);
        String anotherToken = getAnotherMemberToken();
        httpPostWithAuthorization("/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, anotherToken);
        httpPostWithAuthorization("/comments/1/reports", token);

        ExtractableResponse<Response> response = httpPostWithAuthorization("/comments/1/reports", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("존재하지 않는 댓글 신고 요청에 대해 404를 응답한다.")
    @Test
    void reportComment_NotFound() {
        String token = getToken();

        ExtractableResponse<Response> response = httpPostWithAuthorization("/comments/1/reports", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
