package dandi.dandi.batch.weather;

public class WeatherBatchRequest {

    private String key;

    public WeatherBatchRequest() {
    }

    public WeatherBatchRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
