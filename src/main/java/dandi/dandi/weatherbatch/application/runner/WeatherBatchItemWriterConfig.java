package dandi.dandi.weatherbatch.application.runner;

import dandi.dandi.batchcommons.jobparameter.DateTimeJobParameter;
import dandi.dandi.weather.application.port.out.WeatherPersistencePort;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.application.port.out.WeatherRequester;
import dandi.dandi.weather.domain.WeatherLocation;
import dandi.dandi.weather.domain.Weathers;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Configuration
public class WeatherBatchItemWriterConfig {

    private final WeatherPersistencePort weatherPersistencePort;

    public WeatherBatchItemWriterConfig(WeatherPersistencePort weatherPersistencePort) {
        this.weatherPersistencePort = weatherPersistencePort;
    }

    @Bean
    @StepScope
    public ItemWriter<WeatherLocation> weatherItemWriter(WeatherRequester weatherRequester,
                                                         @Qualifier(value = "weatherBatchJobBaseDateTimeParameter") DateTimeJobParameter dateTimeJobParameter) {
        LocalDateTime baseDateTime = dateTimeJobParameter.getLocalDateTime();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        return items -> {
            List<Weathers> weathers = requestWeatherApi(items, baseDateTime, executor, weatherRequester);
            deletePreviousWeathers(items);
            weatherPersistencePort.saveInBatch(weathers);
        };
    }

    private List<Weathers> requestWeatherApi(List<? extends WeatherLocation> weatherLocations, LocalDateTime baseDateTime,
                                             ExecutorService executor, WeatherRequester weatherRequester) {
        List<CompletableFuture<Weathers>> weathersFutures = new ArrayList<>();
        for (WeatherLocation weatherLocation : weatherLocations) {
            CompletableFuture<Weathers> weatherFuture = CompletableFuture.supplyAsync(
                    () -> weatherRequester.getWeathers(baseDateTime, weatherLocation), executor);
            weathersFutures.add(weatherFuture);
        }
        return weathersFutures.stream()
                .map(weatherFuture -> {
                    try {
                        return weatherFuture.get();
                    } catch (InterruptedException e) {
                        throw new WeatherRequestFatalException("(날씨 API Thread InterruptedException)" + e.getMessage());
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private void deletePreviousWeathers(List<? extends WeatherLocation> items) {
        List<Long> locationIds = items.stream()
                .map(WeatherLocation::getId)
                .collect(Collectors.toUnmodifiableList());
        weatherPersistencePort.deleteByLocationIds(locationIds);
    }
}