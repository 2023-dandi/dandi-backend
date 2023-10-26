package dandi.dandi.weatherbatch.application.runner;

import dandi.dandi.batchcommons.jobparameter.DateTimeJobParameter;
import dandi.dandi.weather.application.port.out.WeatherRequester;
import dandi.dandi.weather.domain.WeatherLocation;
import dandi.dandi.weather.domain.Weathers;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class WeatherBatchItemProcessorConfig {

    @Bean
    @StepScope
    public TaskExecutor weatherApiThreadPool(@Value("#{jobParameters[weatherApiThreadSize]}") Long weatherApiThreadSize) {
        ThreadPoolTaskExecutor weatherApiThreadPool = new ThreadPoolTaskExecutor();
        weatherApiThreadPool.setCorePoolSize(weatherApiThreadSize.intValue());
        weatherApiThreadPool.setMaxPoolSize(weatherApiThreadSize.intValue());
        return weatherApiThreadPool;
    }

    @Bean
    @StepScope
    public AsyncItemProcessor<WeatherLocation, Weathers> weatherAsyncItemProcessor(TaskExecutor weatherApiThreadPool) {
        AsyncItemProcessor<WeatherLocation, Weathers> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(weatherItemProcessor(null, null));
        asyncItemProcessor.setTaskExecutor(weatherApiThreadPool);
        return asyncItemProcessor;
    }

    @Bean
    @StepScope
    public ItemProcessor<WeatherLocation, Weathers> weatherItemProcessor(WeatherRequester weatherRequester,
                                                                         DateTimeJobParameter weatherBatchJobBaseDateTimeParameter) {
        return item -> weatherRequester.getWeathers(weatherBatchJobBaseDateTimeParameter.getLocalDateTime(), item);
    }
}
