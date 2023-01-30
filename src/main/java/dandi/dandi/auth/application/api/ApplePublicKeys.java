package dandi.dandi.auth.application.api;

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
