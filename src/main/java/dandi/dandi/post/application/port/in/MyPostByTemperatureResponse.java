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

    private MyPostByTemperatureResponse(Long id, LocalDate createdAt, TemperatureResponse temperatures,
                                        Long feelingIndex,
                                        String postImageUrl, boolean liked) {
        this.id = id;
        this.createdAt = createdAt;
        this.temperatures = temperatures;
        this.feelingIndex = feelingIndex;
        this.postImageUrl = postImageUrl;
        this.liked = liked;
    }

    public MyPostByTemperatureResponse(Post post, boolean liked) {
        this.id = post.getId();
        this.createdAt = post.getCreatedAt();
        this.feelingIndex = post.getWeatherFeelingIndex();
        this.temperatures = new TemperatureResponse(post.getMinTemperature(), post.getMaxTemperature());
        this.liked = liked;
        this.postImageUrl = post.getPostImageUrl();
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

    public MyPostByTemperatureResponse addImageAccessUrl(String imageAccessUrl) {
        return new MyPostByTemperatureResponse(
                id, createdAt, temperatures, feelingIndex, imageAccessUrl + postImageUrl, liked);
    }
}
