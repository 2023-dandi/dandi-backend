package dandi.dandi.batch.image;

import dandi.dandi.batch.exception.BatchException;
import dandi.dandi.image.application.out.ImageManager;
import dandi.dandi.image.application.out.UnusedImagePersistencePort;
import dandi.dandi.image.domain.UnusedImage;
import dandi.dandi.image.exception.ImageDeletionFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class UnusedImageDeletionBatch {

    private static final Logger logger = LoggerFactory.getLogger("asyncLogger");
    private static final String JOB_NAME = "unusedImageDeletion";
    private static final int CHUCK_SIZE = 100;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final UnusedImagePersistencePort unusedImagePersistencePort;
    private final ImageManager imageManager;
    private final DataSource dataSource;

    public UnusedImageDeletionBatch(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, UnusedImagePersistencePort unusedImagePersistencePort, ImageManager imageManager, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.unusedImagePersistencePort = unusedImagePersistencePort;
        this.imageManager = imageManager;
        this.dataSource = dataSource;
    }

    @Bean
    public Job deleteUnusedImageJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(deleteUnusedImageStep(null))
                .build();
    }

    @Bean
    @JobScope
    public Step deleteUnusedImageStep(@Value("#{jobParameters[dateTime]}") String dateTime) {
        logger.info("[{}] Unused ImageDeletion Batch", dateTime);
        return stepBuilderFactory.get("unusedImageDeletion")
                .<UnusedImage, UnusedImage>chunk(CHUCK_SIZE)
                .reader(unusedImageItemReader())
                .writer(unusedImageItemWriter())
                .faultTolerant()
                .retry(ImageDeletionFailedException.class)
                .retryLimit(2)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<UnusedImage> unusedImageItemReader() {
        return new JdbcPagingItemReaderBuilder<UnusedImage>()
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
        queryProvider.setSelectClause("unused_image_id, image_url");
        queryProvider.setFromClause("from unused_image");
        queryProvider.setSortKey("unused_image_id");
        try {
            return queryProvider.getObject();
        } catch (Exception e) {
            throw BatchException.queryProviderObjectCreationFailed(JOB_NAME);
        }
    }

    private RowMapper<UnusedImage> rowMapper() {
        return (rs, rowNum) -> new UnusedImage(rs.getLong("unused_image_id"), rs.getString("image_url"));
    }

    @Bean
    @StepScope
    public ItemWriter<UnusedImage> unusedImageItemWriter() {
        return new CompositeItemWriterBuilder<UnusedImage>()
                .delegates(deleteImageItems(), deleteS3Image())
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<UnusedImage> deleteImageItems() {
        return items -> {
            List<Long> ids = items.stream()
                    .map(UnusedImage::getId)
                    .collect(Collectors.toUnmodifiableList());
            unusedImagePersistencePort.deleteAllBatch(ids);
        };
    }

    @Bean
    @StepScope
    public ItemWriter<UnusedImage> deleteS3Image() {
        return items -> {
            List<String> keys = items.stream()
                    .map(UnusedImage::getImageUrl)
                    .collect(Collectors.toUnmodifiableList());
            imageManager.delete(keys);
        };
    }
}
