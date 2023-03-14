package dandi.dandi.image.adapter.out;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import dandi.dandi.image.application.out.ImageUploader;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AwsS3ImageUploader implements ImageUploader {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public AwsS3ImageUploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket-name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    @Override
    public void upload(String fileKey, InputStream inputStream) throws IOException, SdkClientException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(inputStream.available());
        amazonS3.putObject(bucketName, fileKey, inputStream, objectMetadata);
    }

    @Override
    public void delete(String fileKey) {
        amazonS3.deleteObject(bucketName, fileKey);
    }
}
