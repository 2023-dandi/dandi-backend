package dandi.dandi.batch.weather;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherBatchScheduler {

    private final WeatherBatchExecutor weatherBatchExecutor;

    public WeatherBatchScheduler(WeatherBatchExecutor weatherBatchExecutor) {
        this.weatherBatchExecutor = weatherBatchExecutor;
    }

    @Scheduled(cron = "0 10 2/3 * * *")
    public void runWeatherBatch() {
        weatherBatchExecutor.runWeatherBatch();
    }
}
