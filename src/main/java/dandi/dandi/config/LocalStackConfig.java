package dandi.dandi.config;

import static com.amazonaws.regions.Regions.US_EAST_1;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"local", "test"})
public class LocalStackConfig {

    private final String bucketName;

    public LocalStackConfig(@Value("${cloud.aws.s3.bucket-name}") String bucketName) {
        this.bucketName = bucketName;
    }

    private static final String AWS_REGION = US_EAST_1.getName();
    private static final String AWS_ENDPOINT = "http://localhost:4566";

    private static final String LOCAL_STACK_ACCESS_KEY = "localstack-access-key";
    private static final String LOCAL_STACK_SECRET_KEY = "localstack-secret-key";

    @Bean
    public AmazonS3 amazonS3() {
        AwsClientBuilder.EndpointConfiguration endpoint =
                new AwsClientBuilder.EndpointConfiguration(AWS_ENDPOINT, AWS_REGION);
        BasicAWSCredentials credentials = new BasicAWSCredentials(LOCAL_STACK_ACCESS_KEY, LOCAL_STACK_SECRET_KEY);

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withPathStyleAccessEnabled(true)
                .build();
        amazonS3.createBucket(bucketName);
        return amazonS3;
    }
}
