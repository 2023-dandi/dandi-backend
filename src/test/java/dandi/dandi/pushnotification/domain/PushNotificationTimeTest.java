package dandi.dandi.pushnotification.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PushNotificationTimeTest {

    @DisplayName("10분 단위의 시각으로 PushNotificationTime을 생성할 수 있다.")
    @Test
    void create() {
        LocalTime pushNotificationTime = LocalTime.of(10, 10, 0);

        assertThatCode(() -> PushNotificationTime.from(pushNotificationTime))
                .doesNotThrowAnyException();
    }

    @DisplayName("10분 단위가 아닌 시각으로 PushNotificationTime을 생성하려하면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"10, 1", "11, 0", "11, 1"})
    void create_InvalidPushNotificationTimeUnit(int minute, int second) {
        int hour = 10;
        LocalTime invalidPushNotificationTime = LocalTime.of(hour, minute, second);

        assertThatThrownBy(() -> PushNotificationTime.from(invalidPushNotificationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("푸시 알림 시간은 10분 단위입니다.");
    }
}
