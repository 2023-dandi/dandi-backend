package dandi.dandi.auth.infrastructure.apple;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import dandi.dandi.advice.ExternalServerException;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.auth.infrastructure.apple.dto.ApplePublicKey;
import dandi.dandi.auth.infrastructure.apple.dto.ApplePublicKeys;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class AppleOAuthPublicKeyGeneratorTest {

    private static final String KTY = "RSA";
    private static final String KID = "fh6Bs8C";
    private static final String USE = "sig";
    private static final String ALG = "RS256";
    private static final String N = "u704gotMSZc6CSSVNCZ1d0S9dZKwO2BVzfdTKYz8wSNm7R_KIufOQf3ru7Pph1FjW6gQ8zgvhnv4IebkG"
            + "WsZJlodduTC7c0sRb5PZpEyM6PtO8FPHowaracJJsK1f6_rSLstLdWbSDXeSq7vBvDu3Q31RaoV_0YlEzQwPsbCvD45oVy5Vo5oBePUm"
            + "4cqi6T3cZ-10gr9QJCVwvx7KiQsttp0kUkHM94PlxbG_HAWlEZjvAlxfEDc-_xZQwC6fVjfazs3j1b2DZWsGmBRdx1snO75nM7hpyRRQ"
            + "B4jVejW9TuZDtPtsNadXTr9I5NjxPdIYMORj9XKEh44Z73yfv0gtw";
    private static final String E = "AOAB";

    private final AppleApiCaller appleApiCaller = Mockito.mock(AppleApiCaller.class);
    private final AppleOAuthPublicKeyGenerator appleOAuthPublicKeyGenerator =
            new AppleOAuthPublicKeyGenerator(appleApiCaller);

    @DisplayName("token의 header와 Apple Public Key의 동일한 kid와 alg 값이 없는데 PublicKey를 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"invalidKid, RS256", "fh6Bs8C, invalidAlg", "invalidKid, invalidAlg"})
    void generatePublicKey_InvalidKidOrInvalidAlg(String kid, String alg) {
        List<ApplePublicKey> applePublicKeys = List.of(new ApplePublicKey(KTY, KID, USE, ALG, N, E));
        ApplePublicKeys publicKeys = new ApplePublicKeys(applePublicKeys);
        when(appleApiCaller.getPublicKeys())
                .thenReturn(publicKeys);
        Map<String, String> invalidTokenHeaders = Map.of("kid", kid, "alg", alg);

        assertThatThrownBy(() -> appleOAuthPublicKeyGenerator.generatePublicKey(invalidTokenHeaders))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }

    @DisplayName("Apple에서 받은 유효하지 않은 kty(Key Type) 혹은 N으로 PublicKey를 생성하려 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("provideInvalidKtyAndNAndE")
    void generatePublicKey_InvalidKty(String kty, String n, String e) {
        List<ApplePublicKey> applePublicKeys = List.of(new ApplePublicKey(kty, KID, USE, ALG, n, e));
        ApplePublicKeys publicKeys = new ApplePublicKeys(applePublicKeys);
        when(appleApiCaller.getPublicKeys())
                .thenReturn(publicKeys);
        Map<String, String> tokenHeaders = Map.of("kid", KID, "alg", ALG);

        assertThatThrownBy(() -> appleOAuthPublicKeyGenerator.generatePublicKey(tokenHeaders))
                .isInstanceOf(ExternalServerException.class)
                .hasMessage("응답 받은 Apple Public Key로 PublicKey를 생성할 수 없습니다.");
    }

    private static Stream<Arguments> provideInvalidKtyAndNAndE() {
        return Stream.of(
                Arguments.of(KTY, "invalidN", E),
                Arguments.of("invalidKty", N, E)
        );
    }
}
