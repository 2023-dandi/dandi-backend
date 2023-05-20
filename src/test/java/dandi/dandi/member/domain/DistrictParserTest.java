package dandi.dandi.member.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DistrictParserTest {

    private final DistrictParser districtParser = new DistrictParser();

    @DisplayName("지역구 문자열을 받아 파싱할 수 있다.")
    @ParameterizedTest
    @MethodSource("provideDistrictValueAndExpected")
    void parse(String districtValue, District expected) {
        District actual = districtParser.parse(districtValue);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDistrictValueAndExpected() {
        return Stream.of(
                Arguments.of("대한민국", new District("대한민국")),
                Arguments.of("대한민국 서울특별시", new District("대한민국", "서울특별시")),
                Arguments.of("대한민국 서울특별시 동작구", new District("대한민국", "서울특별시", "동작구"))
        );
    }
}
