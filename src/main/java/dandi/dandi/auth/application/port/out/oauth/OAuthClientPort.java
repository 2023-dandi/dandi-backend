package dandi.dandi.auth.application.port.out.oauth;

public interface OAuthClientPort {

    String getOAuthMemberId(String idToken);
}
