package dandi.dandi.pushnotification.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PushNotificationTest {

    @DisplayName("푸시 알림 시간을 변경한다.")
    @Test
    void updatePushNotificationTime() {
        PushNotification pushNotification = PushNotification.initial(1L);
        LocalTime newPushNotificationTime = LocalTime.of(10, 10);

        pushNotification.updatePushNotificationTime(newPushNotificationTime);
        
        assertThat(pushNotification.getPushNotificationTime()).isEqualTo(newPushNotificationTime);
    }
}
