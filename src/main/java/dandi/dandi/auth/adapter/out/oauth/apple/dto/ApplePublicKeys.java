package dandi.dandi.auth.adapter.out.oauth.apple.dto;

import java.util.List;

public class ApplePublicKeys {

    private List<ApplePublicKey> keys;

    public ApplePublicKeys(List<ApplePublicKey> keys) {
        this.keys = keys;
    }

    private ApplePublicKeys() {
    }

    public List<ApplePublicKey> getKeys() {
        return keys;
    }
}
