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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.retry.backoff.FixedBackOffPolicy;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class WeatherBatch {

    private static final String JOB_NAME = "weatherBatchJob";
    private static final int CHUCK_SIZE = 1000;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final WeatherRequester weatherRequester;
    private final DateTimeJobParameter dateTimeJobParameter;
    private final WeatherPersistencePort weatherPersistencePort;

    public WeatherBatch(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                        DataSource dataSource, WeatherRequester weatherRequester,
                        @Qualifier(value = "weatherBatchJobDateTimeParameter") DateTimeJobParameter dateTimeJobParameter,
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
    public DateTimeJobParameter weatherBatchJobDateTimeParameter(@Value("#{jobParameters[dateTime]}") String dateTime) {
        return new DateTimeJobParameter(dateTime);
    }

    @Bean(JOB_NAME)
    public Job weatherBatch() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(weatherBatchStep(null))
                .build();
    }

    @Bean
    @JobScope
    public Step weatherBatchStep(@Value("#{jobParameters[backOffPeriod]}") Long backOffPeriod) {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(backOffPeriod);
        return stepBuilderFactory.get("weatherInsertion")
                .<WeatherLocation, Weathers>chunk(CHUCK_SIZE)
                .reader(weatherLocationItemReader())
                .processor(weatherItemProcessor())
                .writer(weatherItemWriter())
                .faultTolerant()
                .noRetry(WeatherRequestFatalException.class)
                .retry(WeatherRequestRetryableException.class)
                .retryLimit(2)
                .backOffPolicy(fixedBackOffPolicy)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<WeatherLocation> weatherLocationItemReader() {
        return new JdbcPagingItemReaderBuilder<WeatherLocation>()
                .name("unusedImageItemReader")
                .dataSource(dataSource)
                .pageSize(CHUCK_SIZE)
                .fetchSize(CHUCK_SIZE)
                .queryProvider(pagingQueryProvider())
                .rowMapper(rowMapper())
                .build();
    }

    private PagingQueryProvider pagingQueryProvider() {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("weather_location_id, latitude, longitude, first_district, second_district, third_district");
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
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getString("first_district"),
                rs.getString("second_district"),
                rs.getString("third_district")
        );
    }

    @Bean
    @StepScope
    public ItemProcessor<WeatherLocation, Weathers> weatherItemProcessor() {
        weatherRequester.finish();
        return weatherLocation -> weatherRequester.getWeathers(dateTimeJobParameter.getLocalDateTime(), weatherLocation);
    }

    @Bean
    @StepScope
    public ItemWriter<Weathers> weatherItemWriter() {
        dateTimeJobParameter.getLocalDateTime();
        return items -> {
            List<Weathers> weathers = items.stream()
                    .map(item -> (Weathers) item)
                    .collect(Collectors.toUnmodifiableList());
            weatherPersistencePort.save(weathers);
        };
    }
}
