package dandi.dandi.pushnotification.acceptance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.common.HttpMethodFixture;
import dandi.dandi.pushnotification.application.dto.PushNotificationResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class PushNotificationAcceptanceTest extends AcceptanceTest {

    private static final String LOGIN_REQUEST_URI = "/login/oauth/apple";

    @DisplayName("회원의 푸시 알림 정보(시간대, 허용 여부)의 요청에 성공하면 200과 푸시 알림 정보를 반환한다.")
    @Test
    void getPushNotification() {
        String oAuthIdToken = "idToken";
        when(oAuthClient.getOAuthMemberId(oAuthIdToken))
                .thenReturn("memberIdentifier");
        String token = HttpMethodFixture.httpPost(new LoginRequest(oAuthIdToken), LOGIN_REQUEST_URI)
                .header(AUTHORIZATION);

        ExtractableResponse<Response> response =
                HttpMethodFixture.httpGetWithAuthorization("/push-notification", token);

        PushNotificationResponse pushNotificationResponse = response.jsonPath()
                .getObject(".", PushNotificationResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pushNotificationResponse.getPushNotificationTime()).isEqualTo(LocalTime.MIN),
                () -> assertThat(pushNotificationResponse.isAllowance()).isFalse()
        );
    }
}
