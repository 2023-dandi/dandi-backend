package dandi.dandi.batch.weather;

import dandi.dandi.batch.exception.BatchException;
import dandi.dandi.batch.jobparameter.DateTimeJobParameter;
import dandi.dandi.weather.application.port.out.WeatherPersistencePort;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.application.port.out.WeatherRequestRetryableException;
import dandi.dandi.weather.application.port.out.WeatherRequester;
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
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.retry.backoff.FixedBackOffPolicy;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class WeatherBatch {

    private static final String JOB_NAME = "weatherBatchJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final WeatherRequester weatherRequester;
    private final DateTimeJobParameter dateTimeJobParameter;
    private final WeatherPersistencePort weatherPersistencePort;

    public WeatherBatch(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                        DataSource dataSource, WeatherRequester weatherRequester,
                        @Qualifier(value = "weatherBatchJobBaseDateTimeParameter") DateTimeJobParameter dateTimeJobParameter,
                        WeatherPersistencePort weatherPersistencePort) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.weatherRequester = weatherRequester;
        this.dateTimeJobParameter = dateTimeJobParameter;
        this.weatherPersistencePort = weatherPersistencePort;
    }

    @Bean
    @JobScope
    public DateTimeJobParameter weatherBatchJobBaseDateTimeParameter(@Value("#{jobParameters[baseDateTime]}")
                                                                     String baseDateTime) {
        return new DateTimeJobParameter(baseDateTime);
    }

    @Bean(JOB_NAME)
    public Job weatherBatch() throws IOException {
        return jobBuilderFactory.get(JOB_NAME)
                .start(weatherBatchStep(null, null))
                .build();
    }

    @Bean
    @JobScope
    public Step weatherBatchStep(@Value("#{jobParameters[backOffPeriod]}") Long backOffPeriod,
                                 @Value("#{jobParameters[chunkSize]}") Long chunkSize) {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(backOffPeriod);
        return stepBuilderFactory.get("weatherInsertion")
                .<WeatherLocation, Weathers>chunk(chunkSize.intValue())
                .reader(weatherLocationItemReader(null))
                .processor(weatherItemProcessor())
                .writer(itemWriters())
                .faultTolerant()
                .noRetry(WeatherRequestFatalException.class)
                .retry(WeatherRequestRetryableException.class)
                .retryLimit(2)
                .backOffPolicy(fixedBackOffPolicy)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<WeatherLocation> weatherLocationItemReader(@Value("#{jobParameters[chunkSize]}") Long chunkSize) {
        return new JdbcPagingItemReaderBuilder<WeatherLocation>()
                .name("unusedImageItemReader")
                .dataSource(dataSource)
                .pageSize(chunkSize.intValue())
                .fetchSize(chunkSize.intValue())
                .queryProvider(pagingQueryProvider())
                .rowMapper(rowMapper())
                .build();
    }

    private PagingQueryProvider pagingQueryProvider() {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("weather_location_id, x, y");
        queryProvider.setFromClause("from weather_location");
        queryProvider.setSortKey("weather_location_id");
        try {
            return queryProvider.getObject();
        } catch (Exception e) {
            throw BatchException.queryProviderObjectCreationFailed(JOB_NAME);
        }
    }

    private RowMapper<WeatherLocation> rowMapper() {
        return (rs, rowNum) -> new WeatherLocation(
                rs.getLong("weather_location_id"),
                rs.getInt("x"),
                rs.getInt("y")
        );
    }

    @Bean
    @StepScope
    public ItemProcessor<WeatherLocation, Weathers> weatherItemProcessor() {
        return weatherLocation -> weatherRequester.getWeathers(dateTimeJobParameter.getLocalDateTime(), weatherLocation);
    }

    @Bean
    @StepScope
    public ItemWriter<Weathers> itemWriters() {
        return new CompositeItemWriterBuilder<Weathers>()
                .delegates(previousWeatherItemDeletionWriter(), weatherItemWriter())
                .build();

    }

    @Bean
    @StepScope
    public ItemWriter<Weathers> previousWeatherItemDeletionWriter() {
        return items -> {
            List<Long> locationIds = items.stream()
                    .map(Weathers::getWeatherLocationId)
                    .collect(Collectors.toUnmodifiableList());
            weatherPersistencePort.deleteByLocationIds(locationIds);
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Weathers> weatherItemWriter() {
        return items -> {
            List<Weathers> weathers = items.stream()
                    .map(item -> (Weathers) item)
                    .collect(Collectors.toUnmodifiableList());
            weatherPersistencePort.save(weathers);
        };
    }
}
