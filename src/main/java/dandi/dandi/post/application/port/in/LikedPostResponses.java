package dandi.dandi.post.application.port.in;

import java.util.List;

public class LikedPostResponses {

    private List<LikedPostResponse> posts;
    private boolean lastPage;

    public LikedPostResponses() {
    }

    public LikedPostResponses(List<LikedPostResponse> posts, boolean lastPage) {
        this.posts = posts;
        this.lastPage = lastPage;
    }

    public List<LikedPostResponse> getPosts() {
        return posts;
    }

    public boolean isLastPage() {
        return lastPage;
    }
}
