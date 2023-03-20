package dandi.dandi.post.application.port.in;

import dandi.dandi.post.domain.Post;
import java.time.LocalDate;

public class PostDetailResponse {

    private PostWriterResponse writer;
    private TemperatureResponse temperatures;
    private OutfitFeelingResponse outfitFeelings;
    private String postImageUrl;
    private LocalDate createdAt;

    public PostDetailResponse() {
    }

    public PostDetailResponse(Post post, String imageAccessUrl) {
        this.writer = new PostWriterResponse(post, imageAccessUrl);
        this.temperatures = new TemperatureResponse(post.getMinTemperature(), post.getMaxTemperature());
        this.outfitFeelings =
                new OutfitFeelingResponse(post.getWeatherFeelingIndex(), post.getAdditionalWeatherFeelingIndices());
        this.postImageUrl = imageAccessUrl + post.getPostImageUrl();
        this.createdAt = post.getCreatedAt();
    }

    public PostWriterResponse getWriter() {
        return writer;
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
