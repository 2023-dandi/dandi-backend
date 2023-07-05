package dandi.dandi.post.application.port.in;

import static dandi.dandi.common.constant.Constant.IMAGE_ACCESS_URL;

import dandi.dandi.post.domain.Post;
import java.time.LocalDate;

public class PostDetailResponse {

    private PostWriterResponse writer;
    private boolean mine;
    private boolean liked;
    private TemperatureResponse temperatures;
    private OutfitFeelingResponse outfitFeelings;
    private String postImageUrl;
    private LocalDate createdAt;

    public PostDetailResponse() {
    }

    public PostDetailResponse(Post post, boolean mine, boolean liked) {
        this.writer = new PostWriterResponse(post);
        this.mine = mine;
        this.liked = liked;
        this.temperatures = new TemperatureResponse(post.getMinTemperature(), post.getMaxTemperature());
        this.outfitFeelings =
                new OutfitFeelingResponse(post.getWeatherFeelingIndex(), post.getAdditionalWeatherFeelingIndices());
        this.postImageUrl = System.getProperty(IMAGE_ACCESS_URL) + post.getPostImageUrl();
        this.createdAt = post.getCreatedAt();
    }

    public PostWriterResponse getWriter() {
        return writer;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isLiked() {
        return liked;
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
