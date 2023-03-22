package dandi.dandi.post.application.port.in;

public class MyPostResponse {

    private Long id;
    private String postImageUrl;

    public MyPostResponse() {
    }

    public MyPostResponse(Long id, String postImageUrl) {
        this.id = id;
        this.postImageUrl = postImageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }
}
