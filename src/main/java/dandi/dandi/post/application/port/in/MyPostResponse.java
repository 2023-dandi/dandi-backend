package dandi.dandi.post.application.port.in;

import static dandi.dandi.common.constant.Constant.IMAGE_ACCESS_URL;

import dandi.dandi.post.domain.Post;

public class MyPostResponse {

    private Long id;
    private String postImageUrl;

    public MyPostResponse() {
    }

    public MyPostResponse(Post post) {
        this.id = post.getId();
        this.postImageUrl = System.getProperty(IMAGE_ACCESS_URL) + post.getPostImageUrl();
    }

    public Long getId() {
        return id;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }
}
