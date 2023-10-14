package dandi.dandi.weatherbatch.application.port.in;

public class WeatherBatchRequest {

    private String batchAdminKey;
    private Integer chunkSize;
    private Integer weatherApiThreadSize;

    public WeatherBatchRequest() {
    }

    public WeatherBatchRequest(String batchAdminKey, Integer chunkSize, Integer weatherApiThreadSize) {
        this.batchAdminKey = batchAdminKey;
        this.chunkSize = chunkSize;
        this.weatherApiThreadSize = weatherApiThreadSize;
    }

    public String getBatchAdminKey() {
        return batchAdminKey;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public Integer getWeatherApiThreadSize() {
        return weatherApiThreadSize;
    }
}
