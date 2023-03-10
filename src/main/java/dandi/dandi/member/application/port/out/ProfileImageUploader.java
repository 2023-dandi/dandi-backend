package dandi.dandi.member.application.port.out;

import com.amazonaws.SdkClientException;
import java.io.IOException;
import java.io.InputStream;

public interface ProfileImageUploader {

    void upload(String fileKey, InputStream inputStream) throws IOException, SdkClientException;

    void delete(String fileKey) throws IOException, SdkClientException;
}
