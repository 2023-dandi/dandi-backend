package dandi.dandi.post.application.port.in;

import static dandi.dandi.common.constant.Constant.IMAGE_ACCESS_URL;

import dandi.dandi.post.domain.Post;

public class PostWriterResponse {

    private Long id;
    private String nickname;
    private String profileImageUrl;

    public PostWriterResponse() {
    }

    public PostWriterResponse(Post post) {
        this.id = post.getWriterId();
        this.profileImageUrl = System.getProperty(IMAGE_ACCESS_URL) + post.getWriterProfileImageUrl();
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
