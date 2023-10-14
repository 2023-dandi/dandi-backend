package dandi.dandi.weatherbatch.application.runner;

import dandi.dandi.batchcommons.exception.BatchException;
import dandi.dandi.weather.domain.WeatherLocation;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

import static dandi.dandi.weatherbatch.application.runner.WeatherBatch.WEATHER_BATCH_JOB_NAME;

@Configuration
public class WeatherBatchItemReaderConfig {

    @Bean
    @StepScope
    public ItemReader<WeatherLocation> weatherLocationItemReader(@Value("#{jobParameters[chunkSize]}") Long chunkSize,
                                                                 DataSource dataSource) {
        return new JdbcPagingItemReaderBuilder<WeatherLocation>()
                .name("unusedImageItemReader")
                .dataSource(dataSource)
                .pageSize(chunkSize.intValue())
                .fetchSize(chunkSize.intValue())
                .queryProvider(pagingQueryProvider(dataSource))
                .rowMapper(rowMapper())
                .build();
    }

    private PagingQueryProvider pagingQueryProvider(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("weather_location_id, x, y");
        queryProvider.setFromClause("from weather_location");
        queryProvider.setSortKey("weather_location_id");
        try {
            return queryProvider.getObject();
        } catch (Exception e) {
            throw BatchException.queryProviderObjectCreationFailed(WEATHER_BATCH_JOB_NAME);
        }
    }

    private RowMapper<WeatherLocation> rowMapper() {
        return (rs, rowNum) -> new WeatherLocation(
                rs.getLong("weather_location_id"),
                rs.getInt("x"),
                rs.getInt("y")
        );
    }
}
