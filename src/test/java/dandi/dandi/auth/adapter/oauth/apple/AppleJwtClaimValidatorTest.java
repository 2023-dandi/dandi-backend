package dandi.dandi.auth.adapter.oauth.apple;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import dandi.dandi.auth.adapter.out.oauth.apple.AppleJwtClaimValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class AppleJwtClaimValidatorTest {

    private static final String ISS = "iss";
    private static final String AUD = "aud";

    private final AppleJwtClaimValidator appleJwtClaimValidator = new AppleJwtClaimValidator(ISS, AUD);

    @DisplayName("iss, sub, nonce가 유효한 Claim들인지 반환한다.")
    @CsvSource({"nonce, iss, aud, true", "nonce, invalidIss, aud, false", "nonce, iss, invalidAud, false"})
    @ParameterizedTest
    void isValid(String iss, String aud, boolean expected) {
        Claims claims = Jwts.claims()
                .setIssuer(iss)
                .setAudience(aud);

        boolean actual = appleJwtClaimValidator.isValid(claims);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("만료된 token의 Claim들인지 반환한다.")
    @MethodSource("provideDates")
    @ParameterizedTest
    void isExpired(Date expirationDate, boolean expected) {
        Claims claims = Jwts.claims()
                .setExpiration(expirationDate);

        boolean actual = appleJwtClaimValidator.isExpired(claims);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDates() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = new Date(calendar.getTimeInMillis());

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date tomorrow = new Date(calendar.getTimeInMillis());

        return Stream.of(
                Arguments.of(yesterday, true),
                Arguments.of(tomorrow, false)
        );
    }
}
