package dandi.dandi.config;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
@Profile({"local", "test"})
public class LocalStackConfig {

    private static final DockerImageName LOCAL_STACK_IMAGE = DockerImageName.parse("localstack/localstack");

    private final String bucketName;

    public LocalStackConfig(@Value("${cloud.aws.s3.bucket-name}") String bucketName) {
        this.bucketName = bucketName;
    }


    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer() {
        return new LocalStackContainer(LOCAL_STACK_IMAGE)
                .withServices(S3);
    }

    @Bean
    public AmazonS3 amazonS3(LocalStackContainer localStackContainer) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(S3))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();
        amazonS3.createBucket(bucketName);
        return amazonS3;
    }
}

