package dandi.dandi.post.application.port.in;

import dandi.dandi.post.domain.Post;
import java.time.LocalDate;

public class PostDetailResponse {

    private String writerNickname;
    private TemperatureResponse temperatures;
    private OutfitFeelingResponse outfitFeelings;
    private String postImageUrl;
    private LocalDate createdAt;

    public PostDetailResponse() {
    }

    public PostDetailResponse(Post post, String imageAccessUrl) {
        this.writerNickname = post.getWriterNickname();
        this.temperatures = new TemperatureResponse(post.getMinTemperature(), post.getMaxTemperature());
        this.outfitFeelings = new OutfitFeelingResponse(
                post.getWeatherFeelingIndex(), post.getAdditionalWeatherFeelingIndices());
        this.postImageUrl = imageAccessUrl + post.getPostImageUrl();
        this.createdAt = post.getCreatedAt();
    }

    public String getWriterNickname() {
        return writerNickname;
    }

    public TemperatureResponse getTemperatures() {
        return temperatures;
    }

    public OutfitFeelingResponse getOutfitFeelings() {
        return outfitFeelings;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
