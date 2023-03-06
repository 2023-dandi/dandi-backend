package dandi.dandi.member.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class NicknameUpdateCommandTest {

    private static final String INVALID_NICKNAME_MESSAGE = "닉네임은 공백없이 (.), (-), 영어와 숫자로 이루어진 2 ~ 23자여야 합니다.";

    @DisplayName("공백 없이 (.), (-), 2 - 23자의 영어와 숫자로 이루어진 값으로 NicknameUpdateCommand을 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"1.2", "a-b", "-12", "12.", "12", "ab", "a1", "abcd1234abcd1234abcd123"})
    void create(String nickname) {
        assertThatCode(() -> new NicknameUpdateCommand(nickname))
                .doesNotThrowAnyException();
    }

    @DisplayName("null, 빈문자열, 공백만으로 이루어진 문자열로 NicknameUpdateCommand를 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void create_Null(String nickname) {
        assertThatThrownBy(() -> new NicknameUpdateCommand(nickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_NICKNAME_MESSAGE);
    }

    @DisplayName("2 ~ 23 길이가 아닌 문자열로 NicknameUpdateCommand를 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"a", "abcd1234abcd1234abcd1234"})
    void create_InvalidLength(String invalidCharacterNickname) {
        assertThatThrownBy(() -> new NicknameUpdateCommand(invalidCharacterNickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_NICKNAME_MESSAGE);
    }

    @DisplayName("공백이 포함되어 있거나 (.), (-), 영어와 숫자로 이루어지지 않은 값으로 NicknameUpdateCommand를 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"a 1", "abc.+", "abc닉네임"})
    void create_InvalidCharacter(String invalidLengthNickname) {
        assertThatThrownBy(() -> new NicknameUpdateCommand(invalidLengthNickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_NICKNAME_MESSAGE);
    }
}
