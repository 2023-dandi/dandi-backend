package dandi.dandi.batch.image;

import dandi.dandi.config.LocalStackConfig;
import dandi.dandi.image.adapter.out.AwsS3ImageManager;
import dandi.dandi.image.adapter.out.persistence.jpa.UnusedImagePersistenceAdapter;
import dandi.dandi.image.adapter.out.persistence.jpa.UnusedImageRepository;
import org.junit.runner.RunWith;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBatchTest
@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = "dandi.dandi.image.adapter.out.persistence.jpa")
@EntityScan(basePackages = "dandi.dandi.image.adapter.out.persistence.jpa")
@SpringBootTest(classes = {UnusedImageDeletionBatch.class, AwsS3ImageManager.class, LocalStackConfig.class,
        UnusedImagePersistenceAdapter.class, UnusedImageRepository.class},
        properties = "cloud.aws.s3.bucket-name=test-bucket")
public class UnusedImageDeletionBatchTestConfig {

}
