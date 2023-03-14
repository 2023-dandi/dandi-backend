package dandi.dandi.post.domain;

import java.util.List;

public class Post {

    private final Long id;
    private final Long memberId;
    private final Temperatures temperatures;
    private final String postImageUrl;
    private final WeatherFeeling weatherFeeling;

    public Post(Long id, Long memberId, Temperatures temperatures, String postImageUrl, WeatherFeeling weatherFeeling) {
        this.id = id;
        this.memberId = memberId;
        this.temperatures = temperatures;
        this.postImageUrl = postImageUrl;
        this.weatherFeeling = weatherFeeling;
    }

    public Post(Long memberId, Temperatures temperatures, String postImageUrl, WeatherFeeling weatherFeeling) {
        this(null, memberId, temperatures, postImageUrl, weatherFeeling);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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
}
