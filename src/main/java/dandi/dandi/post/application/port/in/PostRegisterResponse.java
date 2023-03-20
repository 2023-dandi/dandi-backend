package dandi.dandi.post.application.port.in;

public class PostRegisterResponse {

    private Long postId;

    public PostRegisterResponse() {
    }

    public PostRegisterResponse(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }
}
