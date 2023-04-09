package dandi.dandi.notification.acceptance;

import static dandi.dandi.comment.CommentFixture.COMMENT_REGISTER_COMMAND;
import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPatchWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPutWithAuthorization;
import static dandi.dandi.common.RequestURI.NOTIFICATION_REQUEST_URI;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.notification.application.port.in.NotificationResponse;
import dandi.dandi.notification.application.port.in.NotificationResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class NotificationAcceptanceTest extends AcceptanceTest {

    @DisplayName("알림을 조회할 수 있다.")
    @Test
    void getNotifications() {
        String token = getToken();
        Long postId = registerPost(token);
        String anotherToken = getAnotherMemberToken();
        httpPatchWithAuthorization("/posts/" + postId + "/likes", anotherToken);
        httpPostWithAuthorization("/posts/" + postId + "/comments", COMMENT_REGISTER_COMMAND, anotherToken);

        ExtractableResponse<Response> response = httpGetWithAuthorization(NOTIFICATION_REQUEST_URI, token);

        NotificationResponses notificationResponses = response.jsonPath()
                .getObject(".", NotificationResponses.class);
        assertAll(
                () -> assertThat(notificationResponses.isLastPage()).isTrue(),
                () -> assertThat(notificationResponses.getNotifications()).hasSize(2)
        );
    }

    @DisplayName("존재하지 않는 알림의 확인 요청에 대해 404를 응답한다.")
    @Test
    void checkNotification_NotFound() {
        String token = getToken();

        ExtractableResponse<Response> response = httpPutWithAuthorization(NOTIFICATION_REQUEST_URI + "/1/check", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("다른 사용자의 알림 확인 요청에 대해 403을 응답한다.")
    @Test
    void checkNotification_Forbidden() {
        String token = getToken();
        Long postId = registerPost(token);
        String anotherToken = getAnotherMemberToken();
        httpPatchWithAuthorization("/posts/" + postId + "/likes", anotherToken);

        ExtractableResponse<Response> response =
                httpPutWithAuthorization(NOTIFICATION_REQUEST_URI + "/1/check", anotherToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("알림의 확인 여부를 true로 변경 요청에 성공하면 204를 응답한다.")
    @Test
    void checkNotification_NoContent() {
        String token = getToken();
        Long postId = registerPost(token);
        String anotherToken = getAnotherMemberToken();
        httpPatchWithAuthorization("/posts/" + postId + "/likes", anotherToken);
        NotificationResponse notificationBeforeUpdate = getNotification(token);
        String notificationCheckRequestUri =
                NOTIFICATION_REQUEST_URI + "/" + notificationBeforeUpdate.getId() + "/check";

        ExtractableResponse<Response> response = httpPutWithAuthorization(notificationCheckRequestUri, token);

        NotificationResponse notificationAfterUpdate = getNotification(token);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(notificationBeforeUpdate.isChecked()).isFalse(),
                () -> assertThat(notificationAfterUpdate.isChecked()).isTrue()
        );
    }

    private NotificationResponse getNotification(String token) {
        return httpGetWithAuthorization(NOTIFICATION_REQUEST_URI, token)
                .jsonPath()
                .getObject(".", NotificationResponses.class)
                .getNotifications()
                .get(0);
    }
}
