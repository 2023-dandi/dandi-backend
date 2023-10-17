package dandi.dandi.weatherbatch.application.runner;

import dandi.dandi.batchcommons.jobparameter.DateTimeJobParameter;
import dandi.dandi.weather.application.port.out.WeatherPersistencePort;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.application.port.out.WeatherRequestRetryableException;
import dandi.dandi.weather.application.port.out.WeatherRequester;
import dandi.dandi.weather.domain.WeatherLocation;
import dandi.dandi.weather.domain.Weathers;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    public ExecutorService weatherApiThreadPool(@Value("#{jobParameters[weatherApiThreadSize]}") Long weatherApiThreadSize) {
        return Executors.newFixedThreadPool(weatherApiThreadSize.intValue());
    }

    @Bean
    @StepScope
    public ItemWriter<WeatherLocation> weatherItemWriter(WeatherRequester weatherRequester,
                                                         ExecutorService weatherApiThreadPool,
                                                         @Qualifier(value = "weatherBatchJobBaseDateTimeParameter") DateTimeJobParameter dateTimeJobParameter) {
        LocalDateTime baseDateTime = dateTimeJobParameter.getLocalDateTime();
        return items -> {
            List<Weathers> weathers = requestWeatherApi(items, baseDateTime, weatherApiThreadPool, weatherRequester);
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
                .map(this::getFutureValue)
                .collect(Collectors.toUnmodifiableList());
    }

    private Weathers getFutureValue(CompletableFuture<Weathers> weathersFuture) {
        try {
            return weathersFuture.get();
        } catch (InterruptedException e) {
            throw new WeatherRequestFatalException("(날씨 API Thread InterruptedException)" + e.getMessage());
        } catch (ExecutionException e) {
            throw handleExecutionException(e);
        }
    }

    private RuntimeException handleExecutionException(ExecutionException e) {
        if (e.getCause() instanceof WeatherRequestRetryableException) {
            return new WeatherRequestRetryableException(e.getCause().getMessage());
        } else if (e.getCause() instanceof WeatherRequestFatalException) {
            return new WeatherRequestFatalException(e.getCause().getMessage());
        }
        return new RuntimeException(e);
    }

    private void deletePreviousWeathers(List<? extends WeatherLocation> items) {
        List<Long> locationIds = items.stream()
                .map(WeatherLocation::getId)
                .collect(Collectors.toUnmodifiableList());
        weatherPersistencePort.deleteByLocationIds(locationIds);
    }
}
