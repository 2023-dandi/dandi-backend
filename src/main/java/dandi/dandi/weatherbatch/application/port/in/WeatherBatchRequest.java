package dandi.dandi.weatherbatch.application.port.in;

public class WeatherBatchRequest {

    private String batchAdminKey;

    public WeatherBatchRequest() {
    }

    public WeatherBatchRequest(String batchAdminKey) {
        this.batchAdminKey = batchAdminKey;
    }

    public String getBatchAdminKey() {
        return batchAdminKey;
    }
}
