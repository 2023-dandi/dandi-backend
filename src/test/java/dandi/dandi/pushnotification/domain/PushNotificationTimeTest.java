package dandi.dandi.pushnotification.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PushNotificationTimeTest {

    @DisplayName("동일한 값으로 PushNotificationTime을 얻으려고 하면 동일한 주소값의 객체 반환한다.")
    @Test
    void create_Cached() {
        LocalTime localTime = LocalTime.of(10, 10, 0);
        PushNotificationTime firstPushNotificationTime = PushNotificationTime.from(localTime);
        PushNotificationTime secondPushNotificationTime = PushNotificationTime.from(localTime);

        assertThat(firstPushNotificationTime == secondPushNotificationTime).isTrue();
    }
}
