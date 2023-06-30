package dandi.dandi.post.application.port.in;

import dandi.dandi.image.application.in.ImageResponse;
import dandi.dandi.post.domain.Post;
import java.time.LocalDate;

public class PostDetailResponse implements ImageResponse {

    private PostWriterResponse writer;
    private boolean mine;
    private boolean liked;
    private TemperatureResponse temperatures;
    private OutfitFeelingResponse outfitFeelings;
    private String postImageUrl;
    private LocalDate createdAt;

    public PostDetailResponse() {
    }

    private PostDetailResponse(PostWriterResponse writer, boolean mine, boolean liked, TemperatureResponse temperatures,
                               OutfitFeelingResponse outfitFeelings, String postImageUrl, LocalDate createdAt) {
        this.writer = writer;
        this.mine = mine;
        this.liked = liked;
        this.temperatures = temperatures;
        this.outfitFeelings = outfitFeelings;
        this.postImageUrl = postImageUrl;
        this.createdAt = createdAt;
    }

    public PostDetailResponse(Post post, boolean mine, boolean liked) {
        this.writer = new PostWriterResponse(post);
        this.mine = mine;
        this.liked = liked;
        this.temperatures = new TemperatureResponse(post.getMinTemperature(), post.getMaxTemperature());
        this.outfitFeelings =
                new OutfitFeelingResponse(post.getWeatherFeelingIndex(), post.getAdditionalWeatherFeelingIndices());
        this.postImageUrl = post.getPostImageUrl();
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

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        return new PostDetailResponse(
                writer.addImageAccessUrl(imageAccessUrl), mine, liked, temperatures, outfitFeelings,
                imageAccessUrl + postImageUrl, createdAt);
    }
}
