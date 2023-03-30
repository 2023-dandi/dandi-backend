package dandi.dandi.clothes.application.service;

import com.amazonaws.SdkClientException;
import dandi.dandi.clothes.application.port.in.ClothesImageRegisterResponse;
import dandi.dandi.clothes.application.port.in.ClothesImageUseCase;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.image.application.out.ImageManager;
import dandi.dandi.image.exception.ImageDeletionFailedException;
import dandi.dandi.image.exception.ImageUploadFailedException;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClothesImageService implements ClothesImageUseCase {

    private static final String CLOTHES_IMAGE_FILE_KEY_FORMAT = "%s/%s_%s_%s";

    private final ImageManager imageManager;
    private final String clothesImageDir;
    private final String imageAccessUrl;

    public ClothesImageService(ImageManager imageManager,
                               @Value("${image.clothes-dir}") String clothesImageDir,
                               @Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl) {
        this.imageManager = imageManager;
        this.clothesImageDir = clothesImageDir;
        this.imageAccessUrl = imageAccessUrl;
    }

    @Override
    public ClothesImageRegisterResponse uploadClothesImage(Long memberId, MultipartFile multipartFile) {
        String fileKey = generateFileKey(memberId, multipartFile);
        uploadImage(multipartFile, fileKey);
        return new ClothesImageRegisterResponse(imageAccessUrl + fileKey);
    }

    private void uploadImage(MultipartFile multipartFile, String fileKey) {
        try {
            imageManager.upload(fileKey, multipartFile.getInputStream());
        } catch (SdkClientException | IOException e) {
            throw new ImageUploadFailedException();
        }
    }

    private String generateFileKey(Long memberId, MultipartFile profileImage) {
        String uuid = UUID.randomUUID().toString();
        return String.format(CLOTHES_IMAGE_FILE_KEY_FORMAT,
                clothesImageDir, memberId, uuid, profileImage.getOriginalFilename());
    }

    public void deleteClothesImage(Clothes clothes) {
        try {
            imageManager.delete(clothes.getClothesImageUrl());
        } catch (SdkClientException | IOException e) {
            throw new ImageDeletionFailedException();
        }
    }
}
