package dandi.dandi.auth.domain;

public interface OAuthClient {

    String getMemberIdentifier(String idToken);
}
