package dandi.dandi.auth.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class LoginCommandTest {

    private static final String NULL_BLANK_LOGIN_COMMAND_EXCEPTION_MESSAGE = "idToken은 빈 문자열일 수 없습니다.";

    @DisplayName("null, 빈문자열, 공백으로만 이뤄진 문자열로 LoginCommand를 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void create_NullOrEmptyValue(String idToken) {
        assertThatThrownBy(() -> new LoginCommand(idToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NULL_BLANK_LOGIN_COMMAND_EXCEPTION_MESSAGE);
    }
}