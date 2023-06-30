package dandi.dandi.post.application.port.in;

import dandi.dandi.image.application.in.ImageResponse;
import java.util.List;
import java.util.stream.Collectors;

public class FeedResponse implements ImageResponse {

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

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        List<PostResponse> posts = this.posts
                .stream()
                .map(post -> post.addImageAccessUrl(imageAccessUrl))
                .collect(Collectors.toUnmodifiableList());
        return new FeedResponse(posts, lastPage);
    }
}
