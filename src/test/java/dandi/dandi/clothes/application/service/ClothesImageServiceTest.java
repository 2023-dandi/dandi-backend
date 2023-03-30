package dandi.dandi.clothes.application.service;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES;
import static dandi.dandi.utils.image.TestImageUtils.IMAGE_ACCESS_URL;
import static dandi.dandi.utils.image.TestImageUtils.generateTestImgMultipartFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.amazonaws.SdkClientException;
import dandi.dandi.clothes.application.port.in.ClothesImageRegisterResponse;
import dandi.dandi.image.application.out.ImageManager;
import dandi.dandi.image.exception.ImageDeletionFailedException;
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
    private final String clothesImageDir = "clothes";
    private final ClothesImageService clothesImageService =
            new ClothesImageService(imageManager, clothesImageDir, IMAGE_ACCESS_URL);

    @DisplayName("옷 사진을 등록할 수 있다.")
    @Test
    void uploadClothesImage() throws IOException {
        Long memberId = 1L;
        MultipartFile multipartFile = generateTestImgMultipartFile();

        ClothesImageRegisterResponse clothesImageRegisterResponse =
                clothesImageService.uploadClothesImage(memberId, multipartFile);

        verify(imageManager).upload(any(), any());
        assertThat(clothesImageRegisterResponse.getClothesImageUrl())
                .startsWith(IMAGE_ACCESS_URL + clothesImageDir + "/" + memberId);
    }

    @DisplayName("옷 사진 등록에 실패하면 예외를 발생시킨다.")
    @Test
    void uploadClothesImage_Exception() throws IOException {
        Long memberId = 1L;
        MultipartFile multipartFile = generateTestImgMultipartFile();
        doThrow(new SdkClientException("S3 이미지 업로드 실패"))
                .when(imageManager)
                .upload(anyString(), any(InputStream.class));

        assertThatThrownBy(() -> clothesImageService.uploadClothesImage(memberId, multipartFile))
                .isInstanceOf(ImageUploadFailedException.class);
    }

    @DisplayName("옷 사진을 삭제할 수 있다.")
    @Test
    void deleteClothesImage() throws IOException {
        clothesImageService.deleteClothesImage(CLOTHES);

        verify(imageManager).delete(CLOTHES.getClothesImageUrl());
    }

    @DisplayName("옷 사진 삭제에 실패하면 예외를 발생시킨다.")
    @Test
    void deleteClothesImage_DeletionFailed() throws IOException {
        doThrow(new SdkClientException("이미지 삭제 실패"), new IOException())
                .when(imageManager)
                .delete(CLOTHES.getClothesImageUrl());

        assertThatThrownBy(() -> clothesImageService.deleteClothesImage(CLOTHES))
                .isInstanceOf(ImageDeletionFailedException.class);
    }
}
