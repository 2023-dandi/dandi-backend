package dandi.dandi.post.application.port.in;

import java.util.List;

public class FeedResponse {

    private List<PostResponse> posts;
    private boolean lastPage;

    public FeedResponse() {
    }

    public FeedResponse(List<PostResponse> posts, boolean lastPage) {
        this.posts = posts;
        this.lastPage = lastPage;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public boolean isLastPage() {
        return lastPage;
    }
}
