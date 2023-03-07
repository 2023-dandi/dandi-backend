package dandi.dandi.auth.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RefreshTokenTest {

    @DisplayName("만료된 Refresh Token인지 반환한다.")
    @ParameterizedTest
    @MethodSource("provideRefreshTokenAndExpired")
    void isExpired(LocalDateTime expired, boolean expected) {
        RefreshToken refreshToken = new RefreshToken(1L, 1L, expired, "value");

        boolean actual = refreshToken.isExpired();

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideRefreshTokenAndExpired() {
        return Stream.of(
                Arguments.of(LocalDateTime.now().minusHours(1), true),
                Arguments.of(LocalDateTime.now().plusHours(1), false)
        );
    }
}