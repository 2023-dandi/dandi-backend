package dandi.dandi.post.application.port.in;

import java.util.List;

public class MyPostResponses {

    private List<MyPostResponse> posts;

    public MyPostResponses() {
    }

    public MyPostResponses(List<MyPostResponse> posts) {
        this.posts = posts;
    }

    public List<MyPostResponse> getPosts() {
        return posts;
    }
}
