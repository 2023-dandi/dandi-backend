package dandi.dandi.weatherbatch.application.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class WeatherBatchAsyncConfig implements AsyncConfigurer {

    @Bean(name = "weatherBatchApiAsyncExecutor")
    public Executor getAsyncExecutor() {
        return new ThreadPoolExecutor(
                0,
                Integer.MAX_VALUE,
                10,
                TimeUnit.SECONDS,
                new SynchronousQueue<>()
        );
    }
}
