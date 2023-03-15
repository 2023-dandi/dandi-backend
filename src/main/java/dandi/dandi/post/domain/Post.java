package dandi.dandi.post.domain;

import java.util.List;

public class Post {

    private final Long id;
    private final String writerNickname;
    private final Temperatures temperatures;
    private final String postImageUrl;
    private final WeatherFeeling weatherFeeling;

    public Post(Long id, String writerNickname, Temperatures temperatures, String postImageUrl,
                WeatherFeeling weatherFeeling) {
        this.id = id;
        this.writerNickname = writerNickname;
        this.temperatures = temperatures;
        this.postImageUrl = postImageUrl;
        this.weatherFeeling = weatherFeeling;
    }

    public static Post initial(Temperatures temperatures, String postImageUrl, WeatherFeeling weatherFeeling) {
        return new Post(null, null, temperatures, postImageUrl, weatherFeeling);
    }

    public Long getId() {
        return id;
    }

    public String getWriterNickname() {
        return writerNickname;
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
