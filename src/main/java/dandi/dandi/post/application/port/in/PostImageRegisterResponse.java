package dandi.dandi.post.application.port.in;

public class PostImageRegisterResponse {

    private String postImageUrl;

    public PostImageRegisterResponse() {
    }

    public PostImageRegisterResponse(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }
}
