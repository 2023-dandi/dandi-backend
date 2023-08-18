package dandi.dandi.image.adapter.out;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import dandi.dandi.image.application.out.ImageManager;
import dandi.dandi.image.exception.ImageDeletionFailedException;
import dandi.dandi.image.exception.ImageUploadFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

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
        } catch (AmazonClientException | IOException e) {
            throw new ImageUploadFailedException();
        }
    }

    @Override
    public void delete(String fileKey) {
        try {
            amazonS3.deleteObject(bucketName, fileKey);
        } catch (AmazonClientException e) {
            throw new ImageDeletionFailedException(fileKey);
        }
    }

    @Override
    public void delete(List<String> filekeys) {
        try {
            List<DeleteObjectsRequest.KeyVersion> keyVersions = filekeys.stream()
                    .map(DeleteObjectsRequest.KeyVersion::new)
                    .collect(Collectors.toUnmodifiableList());
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName)
                    .withKeys(keyVersions);
            amazonS3.deleteObjects(deleteObjectsRequest);
        } catch (AmazonClientException e) {
            throw new ImageDeletionFailedException(filekeys);
        }
    }
}
