package dandi.dandi.post.application.service;

import com.amazonaws.SdkClientException;
import dandi.dandi.image.application.out.ImageUploader;
import dandi.dandi.image.exception.ImageUploadFailedException;
import dandi.dandi.post.application.port.in.PostImageUseCase;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostImageService implements PostImageUseCase {

    private static final String POST_IMAGE_FILE_KEY_FORMAT = "%s/%s_%s_%s";

    private final ImageUploader imageUploader;
    private final String postImageDir;

    public PostImageService(ImageUploader imageUploader,
                            @Value("${image.post-dir}") String postImageDir) {
        this.imageUploader = imageUploader;
        this.postImageDir = postImageDir;
    }

    @Override
    public String uploadPostImage(Long memberId, MultipartFile multipartFile) {
        String fileKey = generateFileKey(memberId, multipartFile);
        uploadImage(multipartFile, fileKey);
        return fileKey;
    }

    private void uploadImage(MultipartFile multipartFile, String fileKey) {
        try {
            imageUploader.upload(fileKey, multipartFile.getInputStream());
        } catch (SdkClientException | IOException e) {
            throw new ImageUploadFailedException();
        }
    }

    private String generateFileKey(Long memberId, MultipartFile profileImage) {
        String uuid = UUID.randomUUID().toString();
        return String.format(POST_IMAGE_FILE_KEY_FORMAT,
                postImageDir, memberId, uuid, profileImage.getOriginalFilename());
    }
}
