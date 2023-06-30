package dandi.dandi.post.application.port.in;

import dandi.dandi.post.domain.Post;

public class PostWriterResponse {

    private Long id;
    private String nickname;
    private String profileImageUrl;

    public PostWriterResponse() {
    }

    private PostWriterResponse(Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public PostWriterResponse(Post post) {
        this.id = post.getWriterId();
        this.profileImageUrl = post.getWriterProfileImageUrl();
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

    public PostWriterResponse addImageAccessUrl(String imageAccessUrl) {
        return new PostWriterResponse(id, nickname, imageAccessUrl + profileImageUrl);
    }
}
