package dandi.dandi.post.application.port.in;

import dandi.dandi.post.domain.Post;

public class PostWriterResponse {

    private Long id;
    private String nickname;
    private String profileImageUrl;

    public PostWriterResponse() {
    }

    public PostWriterResponse(Post post, String imageAccessUrl) {
        this.id = post.getWriterId();
        this.profileImageUrl = imageAccessUrl + post.getWriterProfileImageUrl();
        this.nickname = post.getWriterNickname();
    }

    public Long getId() {
        return id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getNickname() {
        return nickname;
    }
}
