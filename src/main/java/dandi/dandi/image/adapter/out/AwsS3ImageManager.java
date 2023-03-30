package dandi.dandi.image.adapter.out;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import dandi.dandi.image.application.out.ImageManager;
import dandi.dandi.image.exception.ImageUploadFailedException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AwsS3ImageManager implements ImageManager {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public AwsS3ImageManager(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket-name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    @Override
    public void upload(String fileKey, InputStream inputStream) throws ImageUploadFailedException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            objectMetadata.setContentLength(inputStream.available());
            amazonS3.putObject(bucketName, fileKey, inputStream, objectMetadata);
        } catch (IOException | SdkClientException e) {
            throw new ImageUploadFailedException();
        }
    }

    @Override
    public void delete(String fileKey) {
        try {
            amazonS3.deleteObject(bucketName, fileKey);
        } catch (SdkClientException e) {
            throw new ImageUploadFailedException();
        }
    }
}
