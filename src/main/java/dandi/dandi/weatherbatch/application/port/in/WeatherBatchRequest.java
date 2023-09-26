package dandi.dandi.weatherbatch.application.port.in;

public class WeatherBatchRequest {

    private String batchAdminKey;
    private Integer chunkSize;

    public WeatherBatchRequest() {
    }

    public WeatherBatchRequest(String batchAdminKey, Integer chunkSize) {
        this.batchAdminKey = batchAdminKey;
        this.chunkSize = chunkSize;
    }

    public String getBatchAdminKey() {
        return batchAdminKey;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }
}
