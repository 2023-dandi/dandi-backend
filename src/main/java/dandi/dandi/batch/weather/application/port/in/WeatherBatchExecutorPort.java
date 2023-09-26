package dandi.dandi.batch.weather.application.port.in;

public interface WeatherBatchExecutorPort {

    void runWeatherBatch(WeatherBatchRequest weatherBatchRequest);
}
