package dandi.dandi.weatherbatch.application.service;

import dandi.dandi.weatherbatch.application.port.in.WeatherBatchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherBatchScheduler {

    private final WeatherBatchExecutor weatherBatchExecutor;
    private final String executionKey;

    public WeatherBatchScheduler(WeatherBatchExecutor weatherBatchExecutor,
                                 @Value("${weather.batch.key}") String executionKey) {
        this.weatherBatchExecutor = weatherBatchExecutor;
        this.executionKey = executionKey;
    }

    @Scheduled(cron = "0 10 2/3 * * *")
    public void runWeatherBatch() {
        weatherBatchExecutor.runWeatherBatch(new WeatherBatchRequest(executionKey));
    }
}
