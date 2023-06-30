package dandi.dandi.post.application.port.in;

import dandi.dandi.image.application.in.ImageResponse;
import java.util.List;
import java.util.stream.Collectors;

public class LikedPostResponses implements ImageResponse {

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

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        List<LikedPostResponse> posts = this.posts
                .stream()
                .map(post -> post.addImageAccessUrl(imageAccessUrl))
                .collect(Collectors.toUnmodifiableList());
        return new LikedPostResponses(posts, lastPage);
    }
}
