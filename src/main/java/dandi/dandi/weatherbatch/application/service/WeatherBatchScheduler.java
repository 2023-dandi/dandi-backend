package dandi.dandi.weatherbatch.application.service;

import dandi.dandi.weatherbatch.application.port.in.WeatherBatchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherBatchScheduler {

    private final WeatherBatchExecutor weatherBatchExecutor;
    private final String batchAdminKey;

    public WeatherBatchScheduler(WeatherBatchExecutor weatherBatchExecutor,
                                 @Value("${spring.batch.admin-key}") String batchAdminKey) {
        this.weatherBatchExecutor = weatherBatchExecutor;
        this.batchAdminKey = batchAdminKey;
    }

    @Scheduled(cron = "0 10 2/3 * * *")
    public void runWeatherBatch() {
        weatherBatchExecutor.run(new WeatherBatchRequest(batchAdminKey, null));
    }
}
