package dandi.dandi.weatherbatch.application.service;

import dandi.dandi.weatherbatch.application.port.in.WeatherBatchExecutionPort;
import dandi.dandi.weatherbatch.application.port.in.WeatherBatchRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async("weatherBatchApiAsyncExecutor")
public class WeatherBatchExecutionService implements WeatherBatchExecutionPort {

    private final WeatherBatchExecutor weatherBatchExecutor;

    public WeatherBatchExecutionService(WeatherBatchExecutor weatherBatchExecutor) {
        this.weatherBatchExecutor = weatherBatchExecutor;
    }

    @Override
    public void runWeatherBatch(WeatherBatchRequest weatherBatchRequest) {
        weatherBatchExecutor.run(weatherBatchRequest);
    }
}
