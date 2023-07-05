package dandi.dandi.clothes.application.service;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES;
import static dandi.dandi.utils.TestImageUtils.generateTestImgMultipartFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import dandi.dandi.clothes.application.port.in.ClothesImageRegisterResponse;
import dandi.dandi.image.application.out.ImageManager;
import dandi.dandi.image.application.out.UnusedImagePersistencePort;
import dandi.dandi.image.exception.ImageUploadFailedException;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ClothesImageServiceTest {

    private final ImageManager imageManager = Mockito.mock(ImageManager.class);
    private final UnusedImagePersistencePort unusedImagePersistencePort =
            Mockito.mock(UnusedImagePersistencePort.class);
    private final String clothesImageDir = "clothes";
    private final ClothesImageService clothesImageService =
            new ClothesImageService(imageManager, unusedImagePersistencePort, clothesImageDir);

    @DisplayName("옷 사진을 등록할 수 있다.")
    @Test
    void uploadClothesImage() throws IOException {
        Long memberId = 1L;
        MultipartFile multipartFile = generateTestImgMultipartFile();

        ClothesImageRegisterResponse clothesImageRegisterResponse =
                clothesImageService.uploadClothesImage(memberId, multipartFile);

        assertAll(
                () -> verify(imageManager).upload(any(), any()),
                () -> verify(unusedImagePersistencePort).save(anyString()),
                () -> assertThat(clothesImageRegisterResponse.getClothesImageUrl())
                        .startsWith(clothesImageDir + "/" + memberId)
        );
    }

    @DisplayName("옷 사진 등록에 실패하면 예외를 발생시킨다.")
    @Test
    void uploadClothesImage_Exception() throws IOException {
        Long memberId = 1L;
        MultipartFile multipartFile = generateTestImgMultipartFile();
        doThrow(new ImageUploadFailedException())
                .when(imageManager)
                .upload(anyString(), any(InputStream.class));

        assertAll(
                () -> verify(unusedImagePersistencePort, never()).save(anyString()),
                () -> assertThatThrownBy(() -> clothesImageService.uploadClothesImage(memberId, multipartFile))
                        .isInstanceOf(ImageUploadFailedException.class)
        );
    }

    @DisplayName("옷 사진을 삭제할 수 있다.")
    @Test
    void deleteClothesImage() {
        clothesImageService.deleteClothesImage(CLOTHES);

        verify(imageManager).delete(CLOTHES.getClothesImageUrl());
    }
}
