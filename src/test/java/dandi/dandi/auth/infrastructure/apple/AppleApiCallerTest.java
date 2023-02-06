package dandi.dandi.auth.infrastructure.apple;

import static org.assertj.core.api.Assertions.assertThat;

import dandi.dandi.auth.infrastructure.apple.dto.ApplePublicKey;
import dandi.dandi.auth.infrastructure.apple.dto.ApplePublicKeys;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppleApiCallerTest {

    @Autowired
    private AppleApiCaller appleApiCaller;

    @DisplayName("oauth public keys를 요청 후 응답 받는다.")
    @Test
    void getApplePublicKeys() {
        ApplePublicKeys applePublicKeys = appleApiCaller.getPublicKeys();

        List<ApplePublicKey> keys = applePublicKeys.getKeys();
        boolean isRequestedKeysNonNull = keys.stream()
                .allMatch(this::isAllNotNull);
        assertThat(isRequestedKeysNonNull).isTrue();
    }

    private boolean isAllNotNull(ApplePublicKey applePublicKey) {
        return Objects.nonNull(applePublicKey.getKty()) && Objects.nonNull(applePublicKey.getKid()) &&
                Objects.nonNull(applePublicKey.getUse()) && Objects.nonNull(applePublicKey.getAlg()) &&
                Objects.nonNull(applePublicKey.getN()) && Objects.nonNull(applePublicKey.getE());
    }
}
