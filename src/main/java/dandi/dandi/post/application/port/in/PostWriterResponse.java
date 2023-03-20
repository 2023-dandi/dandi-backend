package dandi.dandi.post.application.port.in;

import dandi.dandi.post.domain.Post;

public class PostWriterResponse {

    private Long id;
    private String nickname;
    private String profileImageUrl;

    public PostWriterResponse() {
    }

    public PostWriterResponse(Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public static PostWriterResponse initialProfilePostWriter(Long id, String nickname) {
        return new PostWriterResponse(id, nickname, null);
    }

    public static PostWriterResponse customProfilePostWriter(Long id, String nickname,
                                                             String profileImageUrl) {
        return new PostWriterResponse(id, nickname, profileImageUrl);
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
