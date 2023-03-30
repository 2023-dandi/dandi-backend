package dandi.dandi.image.application.out;

import com.amazonaws.SdkClientException;
import java.io.IOException;
import java.io.InputStream;

public interface ImageManager {

    void upload(String fileKey, InputStream inputStream) throws IOException, SdkClientException;

    void delete(String fileKey) throws SdkClientException;
}
