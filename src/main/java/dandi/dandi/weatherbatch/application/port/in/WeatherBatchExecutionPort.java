package dandi.dandi.weatherbatch.application.port.in;

public interface WeatherBatchExecutionPort {

    void runWeatherBatch(WeatherBatchRequest weatherBatchRequest);
}
