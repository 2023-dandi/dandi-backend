package dandi.dandi.auth.domain;

public interface OAuthClient {

    String getOAuthMemberId(String idToken);
}
