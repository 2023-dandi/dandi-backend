package dandi.dandi.member.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NicknameTest {

    @DisplayName("공백 없이 (.), (-), 2 - 23자의 영어와 숫자로 이루어진 값으로 Nickname을 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"1.2", "a-b", "-12", "12.", "12", "ab", "a1", "abcd1234abcd1234abcd123"})
    void create(String nickname) {
        assertThatCode(() -> Nickname.from(nickname))
                .doesNotThrowAnyException();
    }

    @DisplayName("2 ~ 23 길이가 아닌 문자열로 Nickname을 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"a", "abcd1234abcd1234abcd1234"})
    void create_InvalidLength(String invalidCharacterNickname) {
        assertThatThrownBy(() -> Nickname.from(invalidCharacterNickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("닉네임은 2 ~ 23자만 가능합니다.");
    }

    @DisplayName("공백이 포함되어 있거나 (.), (-), 영어와 숫자로 이루어지지 않은 값으로 Nickname을 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"a 1", "abc.+", "abc닉네임"})
    void create_InvalidCharacter(String invalidLengthNickname) {
        assertThatThrownBy(() -> Nickname.from(invalidLengthNickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("닉네임에 가능한 문자는 공백없이 (.), (-), 영어와 숫자입니다.");
    }
}
