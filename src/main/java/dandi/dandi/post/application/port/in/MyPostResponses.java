package dandi.dandi.post.application.port.in;

import java.util.List;

public class MyPostResponses {

    private List<MyPostResponse> posts;
    private boolean lastPage;

    public MyPostResponses() {
    }

    public MyPostResponses(List<MyPostResponse> posts, boolean lastPage) {
        this.posts = posts;
        this.lastPage = lastPage;
    }

    public List<MyPostResponse> getPosts() {
        return posts;
    }

    public boolean isLastPage() {
        return lastPage;
    }
}
