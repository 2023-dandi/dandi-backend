package dandi.dandi.post.application.port.in;

import dandi.dandi.post.domain.Post;
import java.time.LocalDate;

public class PostDetailResponse {

    private String writerNickname;
    private TemperatureResponse temperatureResponse;
    private OutfitFeelingResponse outfitFeelingResponse;
    private String postImageUrl;
    private LocalDate createdAt;

    public PostDetailResponse() {
    }

    public PostDetailResponse(Post post) {
        this.writerNickname = post.getWriterNickname();
        this.temperatureResponse = new TemperatureResponse(post.getMinTemperature(), post.getMaxTemperature());
        this.outfitFeelingResponse = new OutfitFeelingResponse(
                post.getWeatherFeelingIndex(), post.getAdditionalWeatherFeelingIndices());
        this.postImageUrl = post.getPostImageUrl();
        this.createdAt = post.getCreatedAt();
    }

    public String getWriterNickname() {
        return writerNickname;
    }

    public TemperatureResponse getTemperatureResponse() {
        return temperatureResponse;
    }

    public OutfitFeelingResponse getOutfitFeelingResponse() {
        return outfitFeelingResponse;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
