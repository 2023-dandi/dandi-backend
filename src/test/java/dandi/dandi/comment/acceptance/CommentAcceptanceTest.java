package dandi.dandi.comment.acceptance;

import static dandi.dandi.comment.CommentFixture.COMMENT_REGISTER_COMMAND;
import static dandi.dandi.common.HttpMethodFixture.httpDeleteWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.comment.application.port.in.CommentResponse;
import dandi.dandi.comment.application.port.in.CommentResponses;
import dandi.dandi.common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CommentAcceptanceTest extends AcceptanceTest {

    private static final String PAGEABLE_QUERY_STRING = "?page=0&size=10&sort=createdAt,DESC";

    @DisplayName("댓글 작성 요청에 성공하면 204를 응답한다.")
    @Test
    void registerComment_NoContent() {
        String token = getToken();
        Long postId = registerPost(token);

        ExtractableResponse<Response> response = httpPostWithAuthorization(
                "/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("존재하지 않는 글에 대한 댓글 작성 요청에 대해 404를 응답한다.")
    @Test
    void registerComment_NotFound() {
        String token = getToken();
        Long notFoundPostId = 1L;

        ExtractableResponse<Response> response = httpPostWithAuthorization(
                "/posts/" + notFoundPostId + "/comments", COMMENT_REGISTER_COMMAND, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("게시글에 해당하는 댓글 조회 요청에 성공하면 200과 댓글들을 응답한다.")
    @Test
    void getComments_OK() {
        String token = getToken();
        String anotherToken = getAnotherMemberToken();
        Long postId = registerPost(token);
        httpPostWithAuthorization("/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, token);
        httpPostWithAuthorization("/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, anotherToken);

        ExtractableResponse<Response> response =
                httpGetWithAuthorization("/posts/" + postId + "/comments" + PAGEABLE_QUERY_STRING, token);

        CommentResponses commentResponses = response.jsonPath()
                .getObject(".", CommentResponses.class);
        List<CommentResponse> comments = commentResponses.getComments();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(commentResponses.isLastPage()).isTrue(),
                () -> assertThat(comments).hasSize(2),
                () -> assertThat(comments.get(0).isPostWriter()).isFalse(),
                () -> assertThat(comments.get(0).isMine()).isFalse(),
                () -> assertThat(comments.get(1).isPostWriter()).isTrue(),
                () -> assertThat(comments.get(1).isMine()).isTrue()
        );
    }

    @DisplayName("존재하지 않는 게시글의 댓글 요청에 대해 404를 응답한다.")
    @Test
    void getComments_NotFount() {
        String token = getToken();
        Long notFountPostId = 1L;

        ExtractableResponse<Response> response =
                httpGetWithAuthorization("/posts/" + notFountPostId + "/comments" + PAGEABLE_QUERY_STRING, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("존재하지 않는 댓글 삭제 요청에 대해 404를 응답한다.")
    @Test
    void deleteComment_NotFound() {
        String token = getToken();

        ExtractableResponse<Response> response = httpDeleteWithAuthorization("/comments/1", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("다른 사용자의 댓글 삭제 요청에 대해 404을 응답한다.")
    @Test
    void deleteComment_Forbidden() {
        String anotherToken = getAnotherMemberToken();
        String token = getToken();
        Long postId = registerPost(token);
        httpPostWithAuthorization("/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, token);

        ExtractableResponse<Response> response = httpDeleteWithAuthorization("/comments/1", anotherToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("자신의 댓글 삭제 요청에 성공하면 204를 응답한다.")
    @Test
    void deleteComment_NoContent() {
        String token = getToken();
        Long postId = registerPost(token);
        httpPostWithAuthorization("/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, token);

        ExtractableResponse<Response> response = httpDeleteWithAuthorization("/comments/1", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("댓글 신고 요청에 성공하면 201을 응답한다.")
    @Test
    void reportComment_Created() {
        String token = getToken();
        Long postId = registerPost(token);
        String anotherToken = getAnotherMemberToken();
        httpPostWithAuthorization("/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, anotherToken);

        ExtractableResponse<Response> response = httpPostWithAuthorization("/comments/1/reports", token);

        String commentsQueryString = "?page=0&size=10&sort=createdAt,DESC";
        List<CommentResponse> commentsAfterReport = httpGetWithAuthorization(
                "/posts/" + postId + "/comments" + commentsQueryString, token)
                .jsonPath()
                .getObject(".", CommentResponses.class)
                .getComments();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(commentsAfterReport).isEmpty()
        );
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
