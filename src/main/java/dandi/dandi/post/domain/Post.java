package dandi.dandi.post.domain;

import dandi.dandi.member.domain.Member;
import java.time.LocalDate;
import java.util.List;

public class Post {

    private final Long id;
    private final Member writer;
    private final Temperatures temperatures;
    private final String postImageUrl;
    private final WeatherFeeling weatherFeeling;
    private final LocalDate createdAt;

    public Post(Long id, Member writer, Temperatures temperatures, String postImageUrl, WeatherFeeling weatherFeeling,
                LocalDate createdAt) {
        this.id = id;
        this.writer = writer;
        this.temperatures = temperatures;
        this.postImageUrl = postImageUrl;
        this.weatherFeeling = weatherFeeling;
        this.createdAt = createdAt;
    }

    public static Post initial(Temperatures temperatures, String postImageUrl, WeatherFeeling weatherFeeling) {
        return new Post(null, null, temperatures, postImageUrl, weatherFeeling, null);
    }

    public Long getId() {
        return id;
    }

    public Double getMinTemperature() {
        return temperatures.getMinTemperature();
    }

    public Double getMaxTemperature() {
        return temperatures.getMaxTemperature();
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public Long getWeatherFeelingIndex() {
        return weatherFeeling.getFeelingIndex();
    }

    public List<Long> getAdditionalWeatherFeelingIndices() {
        return weatherFeeling.getAdditionalFeelingIndices();
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getWriterNickname() {
        return writer.getNickname();
    }

    public Long getWriterId() {
        return writer.getId();
    }

    public String getWriterProfileImageUrl() {
        return writer.getProfileImgUrl();
    }

    public boolean isWrittenBy(Long memberId) {
        return writer.hasId(memberId);
    }
}
