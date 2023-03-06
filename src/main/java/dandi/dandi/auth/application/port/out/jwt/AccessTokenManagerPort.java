package dandi.dandi.auth.application.port.out.jwt;

public interface AccessTokenManagerPort {
    String generateToken(String payload);

    Object getPayload(String token);

    void validate(String token);
}
