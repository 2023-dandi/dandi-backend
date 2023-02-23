package dandi.dandi.pushnotification.acceptance;

import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPatchWithAuthorization;
import static dandi.dandi.common.HttpResponseExtractor.extractExceptionMessage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.common.AcceptanceTest;
import dandi.dandi.pushnotification.application.dto.PushNotificationAllowanceUpdateRequest;
import dandi.dandi.pushnotification.application.dto.PushNotificationResponse;
import dandi.dandi.pushnotification.application.dto.PushNotificationTimeUpdateRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class PushNotificationAcceptanceTest extends AcceptanceTest {

    private static final String PUSH_NOTIFICATION_REQUEST_URI = "/push-notification";
    private static final String PUSH_NOTIFICATION_TIME_REQUEST_URI = "/push-notification/time";
    private static final String PUSH_NOTIFICATION_ALLOWANCE_REQUEST_URI = "/push-notification/allowance";

    @DisplayName("회원의 푸시 알림 정보(시간대, 허용 여부)의 요청에 성공하면 200과 푸시 알림 정보를 반환한다.")
    @Test
    void getPushNotification() {
        String token = getToken();

        ExtractableResponse<Response> response =
                httpGetWithAuthorization(PUSH_NOTIFICATION_REQUEST_URI, token);

        PushNotificationResponse pushNotificationResponse = response.jsonPath()
                .getObject(".", PushNotificationResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pushNotificationResponse.getPushNotificationTime()).isEqualTo(LocalTime.MIN),
                () -> assertThat(pushNotificationResponse.isAllowance()).isFalse()
        );
    }

    @DisplayName("회원의 푸시 알림 시간 변경 요청에 성공하면 204를 반환한다.")
    @Test
    void updatePushNotificationTime_NoContent() {
        String token = getToken();
        LocalTime initialPushNotificationTime = httpGetWithAuthorization(PUSH_NOTIFICATION_REQUEST_URI, token)
                .jsonPath()
                .getObject(".", PushNotificationResponse.class)
                .getPushNotificationTime();
        PushNotificationTimeUpdateRequest pushNotificationTimeUpdateRequest =
                new PushNotificationTimeUpdateRequest(LocalTime.of(20, 10));

        ExtractableResponse<Response> response = httpPatchWithAuthorization(
                PUSH_NOTIFICATION_TIME_REQUEST_URI, pushNotificationTimeUpdateRequest, token);

        LocalTime pushNotificationTimeAfterUpdate = httpGetWithAuthorization(PUSH_NOTIFICATION_REQUEST_URI, token)
                .jsonPath()
                .getObject(".", PushNotificationResponse.class)
                .getPushNotificationTime();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(initialPushNotificationTime).isNotEqualTo(pushNotificationTimeAfterUpdate)
        );
    }

    @DisplayName("10분 단위가 아닌 푸시 알림 시간 변경 요청에 대해 400을 반환한다.")
    @Test
    void updatePushNotificationTime_BadRequest() {
        String token = getToken();
        PushNotificationTimeUpdateRequest invalidPushNotificationTimeUpdateRequest =
                new PushNotificationTimeUpdateRequest(LocalTime.of(20, 11));

        ExtractableResponse<Response> response = httpPatchWithAuthorization(
                PUSH_NOTIFICATION_TIME_REQUEST_URI, invalidPushNotificationTimeUpdateRequest, token);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(extractExceptionMessage(response)).isNotNull()
        );
    }

    @DisplayName("푸시 알림 허용 여부를 변경할 수 있다.")
    @Test
    void updatePushNotificationAllowance() {
        String token = getToken();
        boolean initialAllowance = httpGetWithAuthorization(PUSH_NOTIFICATION_REQUEST_URI, token)
                .jsonPath()
                .getObject(".", PushNotificationResponse.class)
                .isAllowance();
        PushNotificationAllowanceUpdateRequest pushNotificationAllowanceUpdateRequest =
                new PushNotificationAllowanceUpdateRequest(true);

        ExtractableResponse<Response> response = httpPatchWithAuthorization(
                PUSH_NOTIFICATION_ALLOWANCE_REQUEST_URI, pushNotificationAllowanceUpdateRequest, token);

        boolean allowanceAfterUpdate = httpGetWithAuthorization(PUSH_NOTIFICATION_REQUEST_URI, token)
                .jsonPath()
                .getObject(".", PushNotificationResponse.class)
                .isAllowance();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(initialAllowance).isNotEqualTo(allowanceAfterUpdate)
        );
    }
}
