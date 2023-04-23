package dandi.dandi.auth.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LoginCommandTest {

    @DisplayName("null, 빈문자열, 공백으로만 이뤄진 값들로 LoginCommand를 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("provideIdTokenAndPushNotificationToken")
    void create_NullOrEmptyValue(String idToken, String pushNotificationToken) {
        assertThatThrownBy(() -> new LoginCommand(idToken, pushNotificationToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("idToken 혹은 pushNotificationToken은 빈 문자열일 수 없습니다.");
    }

    private static Stream<Arguments> provideIdTokenAndPushNotificationToken() {
        return Stream.of(
                Arguments.of("", "pushNotificationToken"),
                Arguments.of(" ", "pushNotificationToken"),
                Arguments.of(null, "pushNotificationToken"),
                Arguments.of("idToken", ""),
                Arguments.of("idToken", " "),
                Arguments.of(null, " ")
        );
    }
}
