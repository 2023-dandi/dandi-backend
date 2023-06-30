package dandi.dandi.post.application.port.in;

import dandi.dandi.post.domain.Post;

public class MyPostResponse {

    private Long id;
    private String postImageUrl;

    public MyPostResponse() {
    }

    private MyPostResponse(Long id, String postImageUrl) {
        this.id = id;
        this.postImageUrl = postImageUrl;
    }

    public MyPostResponse(Post post) {
        this.id = post.getId();
        this.postImageUrl = post.getPostImageUrl();
    }

    public Long getId() {
        return id;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public MyPostResponse addImageAccessUrl(String imageAccessUrl) {
        return new MyPostResponse(id, imageAccessUrl + postImageUrl);
    }
}
