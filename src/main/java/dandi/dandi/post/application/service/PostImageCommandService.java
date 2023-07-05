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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostImageCommandService implements PostImageCommandPort {

    private static final String POST_IMAGE_FILE_KEY_FORMAT = "%s/%s_%s_%s";

    private final ImageManager imageManager;
    private final UnusedImagePersistencePort unusedImagePersistencePort;
    private final String postImageDir;

    public PostImageCommandService(ImageManager imageManager, UnusedImagePersistencePort unusedImagePersistencePort,
                                   @Value("${image.post-dir}") String postImageDir) {
        this.imageManager = imageManager;
        this.unusedImagePersistencePort = unusedImagePersistencePort;
        this.postImageDir = postImageDir;
    }

    @Override
    @Transactional
    public PostImageRegisterResponse uploadPostImage(Long memberId, MultipartFile multipartFile) {
        String fileKey = generateFileKey(memberId, multipartFile);
        unusedImagePersistencePort.save(fileKey);
        uploadImage(multipartFile, fileKey);
        return new PostImageRegisterResponse(fileKey);
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

    @Transactional(propagation = Propagation.MANDATORY)
    public void deletePostImageUrlInUnused(String imageUrl) {
        unusedImagePersistencePort.delete(imageUrl);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deletePostImage(String imageUrl) {
        imageManager.delete(imageUrl);
    }
}
