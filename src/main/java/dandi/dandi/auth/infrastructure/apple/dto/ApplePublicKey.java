package dandi.dandi.auth.infrastructure.apple.dto;

public class ApplePublicKey {

    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String n;
    private String e;

    public ApplePublicKey(String kty, String kid, String use, String alg, String n, String e) {
        this.kty = kty;
        this.kid = kid;
        this.use = use;
        this.alg = alg;
        this.n = n;
        this.e = e;
    }

    private ApplePublicKey() {
    }

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
