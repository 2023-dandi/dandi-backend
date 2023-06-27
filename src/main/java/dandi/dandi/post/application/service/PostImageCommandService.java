package dandi.dandi.post.application.service;

import dandi.dandi.image.application.out.ImageManager;
import dandi.dandi.image.application.out.UnusedImagePersistencePort;
import dandi.dandi.image.exception.ImageUploadFailedException;
import dandi.dandi.post.application.port.in.PostImageCommandPort;
import dandi.dandi.post.application.port.in.PostImageRegisterResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostImageCommandService implements PostImageCommandPort {

    private static final String POST_IMAGE_FILE_KEY_FORMAT = "%s/%s_%s_%s";

    private final ImageManager imageManager;
    private final UnusedImagePersistencePort unusedImagePersistencePort;
    private final String postImageDir;
    private final String imageAccessUrl;

    public PostImageCommandService(ImageManager imageManager, UnusedImagePersistencePort unusedImagePersistencePort,
                                   @Value("${image.post-dir}") String postImageDir,
                                   @Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl) {
        this.imageManager = imageManager;
        this.unusedImagePersistencePort = unusedImagePersistencePort;
        this.postImageDir = postImageDir;
        this.imageAccessUrl = imageAccessUrl;
    }

    @Override
    public PostImageRegisterResponse uploadPostImage(Long memberId, MultipartFile multipartFile) {
        String fileKey = generateFileKey(memberId, multipartFile);
        uploadImage(multipartFile, fileKey);
        unusedImagePersistencePort.save(fileKey);
        return new PostImageRegisterResponse(imageAccessUrl + fileKey);
    }

    private void uploadImage(MultipartFile multipartFile, String fileKey) {
        try {
            imageManager.upload(fileKey, multipartFile.getInputStream());
        } catch (IOException e) {
            throw new ImageUploadFailedException();
        }
    }

    private String generateFileKey(Long memberId, MultipartFile profileImage) {
        String uuid = UUID.randomUUID().toString();
        return String.format(POST_IMAGE_FILE_KEY_FORMAT,
                postImageDir, memberId, uuid, profileImage.getOriginalFilename());
    }

    public void deletePostImage(String imageUrl) {
        imageManager.delete(imageUrl);
    }
}
