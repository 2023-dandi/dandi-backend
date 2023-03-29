package dandi.dandi.post.application.port.in;

import dandi.dandi.post.domain.Post;
import java.time.LocalDate;

public class MyPostByTemperatureResponse {

    private Long id;
    private LocalDate createdAt;
    private TemperatureResponse temperatures;
    private Long feelingIndex;
    private String postImageUrl;
    private boolean liked;

    public MyPostByTemperatureResponse() {
    }

    public MyPostByTemperatureResponse(Post post, boolean liked, String imageAccessUrl) {
        this.id = post.getId();
        this.createdAt = post.getCreatedAt();
        this.feelingIndex = post.getWeatherFeelingIndex();
        this.temperatures = new TemperatureResponse(post.getMinTemperature(), post.getMaxTemperature());
        this.liked = liked;
        this.postImageUrl = imageAccessUrl + post.getPostImageUrl();
    }

    public Long getId() {
        return id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public TemperatureResponse getTemperatures() {
        return temperatures;
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
