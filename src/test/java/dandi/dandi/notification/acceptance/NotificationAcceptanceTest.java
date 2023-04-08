package dandi.dandi.notification.acceptance;

import static dandi.dandi.comment.CommentFixture.COMMENT_REGISTER_COMMAND;
import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPatchWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static dandi.dandi.common.RequestURI.NOTIFICATION_REQUEST_URI;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.notification.application.port.in.NotificationResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
