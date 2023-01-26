package dandi.dandi.auth.application.api;

public class ApplePublicKey {

    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String n;
    private String e;

    public String getKty() {
        return kty;
    }

    public String getKid() {
        return kid;
    }

    public String getUse() {
        return use;
    }

    public String getAlg() {
        return alg;
    }

    public String getN() {
        return n;
    }

    public String getE() {
        return e;
    }
}
