package dandi.dandi.post.application.port.in;

import dandi.dandi.post.domain.Post;

public class MyPostByTemperatureResponse {

    private Long id;
    private Long feelingIndex;
    private String postImageUrl;
    private boolean liked;

    public MyPostByTemperatureResponse() {
    }

    public MyPostByTemperatureResponse(Post post, boolean liked, String imageAccessUrl) {
        this.id = post.getId();
        this.feelingIndex = post.getWeatherFeelingIndex();
        this.liked = liked;
        this.postImageUrl = imageAccessUrl + post.getPostImageUrl();
    }

    public Long getId() {
        return id;
    }

    public Long getFeelingIndex() {
        return feelingIndex;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public boolean isLiked() {
        return liked;
    }
}
