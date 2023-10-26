package dandi.dandi.weatherbatch.application.runner;

import dandi.dandi.batchcommons.jobparameter.DateTimeJobParameter;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.application.port.out.WeatherRequestRetryableException;
import dandi.dandi.weather.domain.WeatherLocation;
import dandi.dandi.weather.domain.Weathers;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.io.IOException;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.Future;

@Configuration
public class WeatherBatch {

    public static final String WEATHER_BATCH_JOB_NAME = "weatherBatchJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public WeatherBatch(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    @StepScope
    public DateTimeJobParameter weatherBatchJobBaseDateTimeParameter(@Value("#{jobParameters[baseDateTime]}")
                                                                     String baseDateTime) {
        return new DateTimeJobParameter(baseDateTime);
    }

    @Bean(WEATHER_BATCH_JOB_NAME)
    public Job weatherBatch() throws IOException {
        return jobBuilderFactory.get(WEATHER_BATCH_JOB_NAME)
                .start(weatherBatchStep(null, null, null, null, null))
                .build();
    }

    @Bean
    @JobScope
    public Step weatherBatchStep(@Value("#{jobParameters[backOffPeriod]}") Long backOffPeriod,
                                 @Value("#{jobParameters[chunkSize]}") Long chunkSize,
                                 ItemReader<WeatherLocation> weatherLocationItemReader,
                                 ItemProcessor<WeatherLocation, Future<Weathers>> weatherAsyncItemProcessor,
                                 ItemWriter<Future<Weathers>> weathersAsyncItemWriter) {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(backOffPeriod);
        return stepBuilderFactory.get("weatherInsertion")
                .<WeatherLocation, Future<Weathers>>chunk(chunkSize.intValue())
                .reader(weatherLocationItemReader)
                .processor(weatherAsyncItemProcessor)
                .writer(weathersAsyncItemWriter)
                .faultTolerant()
                .retryPolicy(retryPolicy())
                .backOffPolicy(fixedBackOffPolicy)
                .build();
    }

    private RetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = Map.of(
                WeatherRequestRetryableException.class, true,
                SocketException.class, true,
                WeatherRequestFatalException.class, false);
        return new SimpleRetryPolicy(3, retryableExceptions);
    }
}
