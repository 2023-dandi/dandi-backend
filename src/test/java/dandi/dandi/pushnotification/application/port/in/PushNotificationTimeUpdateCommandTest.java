package dandi.dandi.pushnotification.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

class PushNotificationTimeUpdateCommandTest {

    private static final String NULL_PUSH_NOTIFICATION_TIME_EXCEPTION_MESSAGE = "푸시 알림 변경 시간이 존재하지 않습니다.";
    private static final String INVALID_PUSH_NOTIFICATION_TIME_UNIT_EXCEPTION_MESSAGE = "푸시 알림 시간은 10분 단위입니다.";

    @DisplayName("null 값으로 PushNotificationTimeUpdateCommand을 생성하려하면 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    void create_Null(LocalTime pushNotificationTime) {
        assertThatThrownBy(() -> new PushNotificationTimeUpdateCommand(pushNotificationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NULL_PUSH_NOTIFICATION_TIME_EXCEPTION_MESSAGE);
    }

    @DisplayName("10분 단위가 아닌 시각으로 PushNotificationTimeUpdateCommand을 생성하려하면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"10, 1", "11, 0", "11, 1"})
    void create_InvalidPushNotificationTimeUnit(int minute, int second) {
        int hour = 10;
        LocalTime invalidPushNotificationTime = LocalTime.of(hour, minute, second);

        assertThatThrownBy(() -> new PushNotificationTimeUpdateCommand(invalidPushNotificationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_PUSH_NOTIFICATION_TIME_UNIT_EXCEPTION_MESSAGE);
    }
}