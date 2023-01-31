package dandi.dandi.auth.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.auth.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtParserTest {

    private static final String TOKEN = "eyJraWQiOiI4NkQ4OEtmIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQu"
            + "YXBwbGUuY29tIiwiYXVkIjoiY29tLndoaXRlcGFlay5zZXJ2aWNlcyIsImV4cCI6MTU5ODgwMDEyOCwiaWF0IjoxNTk4Nzk5NTI4LC"
            + "JzdWIiOiIwMDAxNDguZjA2ZDgyMmNlMGIyNDgzYWFhOTdkMjczYjA5NzgzMjUuMTcxNyIsIm5vbmNlIjoiMjBCMjBELTBTOC0xSzgiL"
            + "CJjX2hhc2giOiJ1aFFiV0gzQUFWdEc1OUw4eEpTMldRIiwiZW1haWwiOiJpNzlmaWl0OWIzQHByaXZhdGVyZWxheS5hcHBsZWlkLmNv"
            + "bSIsImVtYWlsX3ZlcmlmaWVkIjoidHJ1ZSIsImlzX3ByaXZhdGVfZW1haWwiOiJ0cnVlIiwiYXV0aF90aW1lIjoxNTk4Nzk5NTI4LCJ"
            + "ub25jZV9zdXBwb3J0ZWQiOnRydWV9.GQBCUHza0yttOfpQ-J5OvyZoGe5Zny8pI06sKVDIJaQY3bdiphllg1_pHMtPUp7FLv3ccthcm"
            + "qmZn7NWVoIPkc9-_8squ_fp9F68XM-UsERKVzBvVR92TwQuKOPFr4lRn-2FlBzN4NegicMS-IV8Ad3AKTIRMIhvAXG4UgNxgPAuCpHw"
            + "CwEAJijljfUfnRYO-_ywgTcF26szluBz9w0Y1nn_IIVCUzAwYiEMdLo53NoyJmWYFWu8pxmXRpunbMHl5nvFpf9nK-OGtMJrmZ4DlpT"
            + "c2Gv64Zs2bwHDEvOyQ1WiRUB6_FWRH5FV10JSsccMlm6iOByOLYd03RRH2uYtFw";
    private static final String ALGORITHM = "RSA";

    private final JwtParser jwtParser = new JwtParser();

    @DisplayName("token을 받아 header를 반환한다.")
    @Test
    void parseHeaders() {
        Map<String, String> headers = jwtParser.parseHeaders(TOKEN);

        assertThat(headers).containsKeys("alg", "kid");
    }

    @DisplayName("유효하지 않은 token의 header를 추출하려고 하면 예외를 발생시킨다.")
    @Test
    void parseHeaders_InvalidToken() {
        String invalidToken = "invalidToken";

        assertThatThrownBy(() -> jwtParser.parseHeaders(invalidToken))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }

    @DisplayName("token과 PublicKey를 받아 Claim들을 반환한다.")
    @Test
    void parseClaims() throws NoSuchAlgorithmException {
        KeyPair keyPair = KeyPairGenerator.getInstance(ALGORITHM)
                .generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        Map<String, Object> headers = Map.of("kid", "86D88Kf", "alg", "RS256");
        String token = Jwts.builder()
                .setHeader(headers)
                .setSubject("subject")
                .signWith(privateKey)
                .compact();

        Claims claims = jwtParser.parseClaims(token, publicKey);

        assertThat(claims.getSubject()).isEqualTo("subject");
    }

    @DisplayName("PublicKey로 복호화할 수 없는 token의 Claim들을 추출하려고 하면 예외를 발생시킨다.")
    @Test
    void parseClaims_InvalidPublicKey() throws NoSuchAlgorithmException {
        PrivateKey privateKey = KeyPairGenerator.getInstance(ALGORITHM)
                .generateKeyPair()
                .getPrivate();
        PublicKey anotherPublicKey = KeyPairGenerator.getInstance(ALGORITHM)
                .generateKeyPair()
                .getPublic();
        String token = Jwts.builder()
                .setSubject("subject")
                .signWith(privateKey)
                .compact();

        assertThatThrownBy(() -> jwtParser.parseClaims(token, anotherPublicKey))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }
}
