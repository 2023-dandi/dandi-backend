package dandi.dandi.auth.infrastructure.apple;

import dandi.dandi.advice.ExternalServerException;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.auth.infrastructure.apple.dto.ApplePublicKey;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
public class AppleOAuthPublicKeyGenerator {

    private static final String ALG_HEADER_KEY = "alg";
    private static final String KID_HEADER_KEY = "kid";
    private static final int POSITIVE_SIGNUM = 1;

    private final AppleApiCaller appleApiCaller;

    public AppleOAuthPublicKeyGenerator(AppleApiCaller appleApiCaller) {
        this.appleApiCaller = appleApiCaller;
    }

    public PublicKey generatePublicKey(Map<String, String> tokenHeaders) {
        List<ApplePublicKey> publicKeys = appleApiCaller.getPublicKeys()
                .getKeys();
        ApplePublicKey publicKey = publicKeys.stream()
                .filter(key -> key.getAlg().equals(tokenHeaders.get(ALG_HEADER_KEY)))
                .filter(key -> key.getKid().equals(tokenHeaders.get(KID_HEADER_KEY)))
                .findAny()
                .orElseThrow(UnauthorizedException::invalid);

        return generatePublicKeyWithApplePublicKey(publicKey);
    }

    private PublicKey generatePublicKeyWithApplePublicKey(ApplePublicKey applePublicKey) {
        byte[] n = Base64Utils.decodeFromUrlSafeString(applePublicKey.getN());
        byte[] e = Base64Utils.decodeFromUrlSafeString(applePublicKey.getE());
        RSAPublicKeySpec publicKeySpec =
                new RSAPublicKeySpec(new BigInteger(POSITIVE_SIGNUM, n), new BigInteger(POSITIVE_SIGNUM, e));

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new ExternalServerException("응답 받은 Apple Public Key로 PublicKey를 생성할 수 없습니다.");
        }
    }
}
