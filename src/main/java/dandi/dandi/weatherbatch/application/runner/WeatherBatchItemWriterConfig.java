package dandi.dandi.weatherbatch.application.runner;

import dandi.dandi.weather.application.port.out.WeatherPersistencePort;
import dandi.dandi.weather.domain.Weathers;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Configuration
public class WeatherBatchItemWriterConfig {

    private final WeatherPersistencePort weatherPersistencePort;

    public WeatherBatchItemWriterConfig(WeatherPersistencePort weatherPersistencePort) {
        this.weatherPersistencePort = weatherPersistencePort;
    }

    @Bean
    @StepScope
    public ItemWriter<Future<Weathers>> weathersAsyncItemWriter() {
        AsyncItemWriter<Weathers> weathersAsyncItemWriter = new AsyncItemWriter<>();
        weathersAsyncItemWriter.setDelegate(weatherItemWriter());
        return weathersAsyncItemWriter;
    }

    @Bean
    @StepScope
    public ItemWriter<Weathers> weatherItemWriter() {
        return items -> {
            deletePreviousWeathers(items);
            weatherPersistencePort.saveInBatch(items);
        };
    }

    private void deletePreviousWeathers(List<? extends Weathers> items) {
        List<Long> locationIds = items.stream()
                .map(Weathers::getWeatherLocationId)
                .collect(Collectors.toUnmodifiableList());
        weatherPersistencePort.deleteByLocationIds(locationIds);
    }
}
