package dandi.dandi.post.application.port.in;

import dandi.dandi.post.domain.Post;
import java.time.LocalDate;

public class LikedPostResponse {

    private Long id;
    private PostWriterResponse writer;
    private TemperatureResponse temperatures;
    private long feelingIndex;
    private String postImageUrl;
    private LocalDate createdAt;

    public LikedPostResponse() {
    }

    private LikedPostResponse(Long id, PostWriterResponse writer, TemperatureResponse temperatures, long feelingIndex,
                              String postImageUrl, LocalDate createdAt) {
        this.id = id;
        this.writer = writer;
        this.temperatures = temperatures;
        this.feelingIndex = feelingIndex;
        this.postImageUrl = postImageUrl;
        this.createdAt = createdAt;
    }

    public LikedPostResponse(Post post) {
        this.id = post.getId();
        this.writer = new PostWriterResponse(post);
        this.temperatures = new TemperatureResponse(post.getMinTemperature(), post.getMaxTemperature());
        this.feelingIndex = post.getWeatherFeelingIndex();
        this.postImageUrl = post.getPostImageUrl();
        this.createdAt = post.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public PostWriterResponse getWriter() {
        return writer;
    }

    public TemperatureResponse getTemperatures() {
        return temperatures;
    }

    public long getFeelingIndex() {
        return feelingIndex;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LikedPostResponse addImageAccessUrl(String imageAccessUrl) {
        return new LikedPostResponse(
                id,
                writer.addImageAccessUrl(imageAccessUrl),
                temperatures,
                feelingIndex,
                imageAccessUrl + postImageUrl,
                createdAt
        );
    }
}
