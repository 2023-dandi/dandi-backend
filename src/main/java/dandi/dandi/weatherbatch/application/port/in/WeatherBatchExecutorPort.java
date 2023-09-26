package dandi.dandi.weatherbatch.application.port.in;

public interface WeatherBatchExecutorPort {

    void runWeatherBatch(WeatherBatchRequest weatherBatchRequest);
}
