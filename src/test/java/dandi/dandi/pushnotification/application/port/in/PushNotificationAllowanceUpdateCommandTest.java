package dandi.dandi.pushnotification.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class PushNotificationAllowanceUpdateCommandTest {

    @DisplayName("null 값으로 PushNotificationAllowanceUpdateCommand를 생성하려하면 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    void create_Null(Boolean allowance) {
        assertThatThrownBy(() -> new PushNotificationAllowanceUpdateCommand(allowance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("푸시 알림 허용 여부가 입력되지 않았습니다.");
    }
}
