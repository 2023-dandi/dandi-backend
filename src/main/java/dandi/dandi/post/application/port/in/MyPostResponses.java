package dandi.dandi.post.application.port.in;

import dandi.dandi.image.application.in.ImageResponse;
import java.util.List;
import java.util.stream.Collectors;

public class MyPostResponses implements ImageResponse {

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

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        List<MyPostResponse> posts = this.posts
                .stream()
                .map(post -> post.addImageAccessUrl(imageAccessUrl))
                .collect(Collectors.toUnmodifiableList());
        return new MyPostResponses(posts, lastPage);
    }
}
