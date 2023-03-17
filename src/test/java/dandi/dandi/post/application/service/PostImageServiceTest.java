package dandi.dandi.post.application.service;

import static dandi.dandi.utils.image.TestImageUtils.IMAGE_ACCESS_URL;
import static dandi.dandi.utils.image.TestImageUtils.generateTestImgMultipartFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import dandi.dandi.image.application.out.ImageUploader;
import dandi.dandi.image.exception.ImageUploadFailedException;
import dandi.dandi.post.application.port.in.PostImageRegisterResponse;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class PostImageServiceTest {

    private final String postImageDir = "post-image";
    private final ImageUploader imageUploader = Mockito.mock(ImageUploader.class);
    private final PostImageService postImageService =
            new PostImageService(imageUploader, postImageDir, IMAGE_ACCESS_URL);

    @DisplayName("게시글 사진을 업로드할 수 있다.")
    @Test
    void uploadPostImage() throws IOException {
        Long memberId = 1L;
        MultipartFile multipartFile = generateTestImgMultipartFile();

        PostImageRegisterResponse postImageRegisterResponse = postImageService.uploadPostImage(memberId, multipartFile);

        assertAll(
                () -> verify(imageUploader).upload(anyString(), any(InputStream.class)),
                () -> assertThat(postImageRegisterResponse.getPostImageUrl())
                        .startsWith(IMAGE_ACCESS_URL + postImageDir)
                        .contains(multipartFile.getOriginalFilename())
                        .contains(String.valueOf(memberId))
        );
    }

    @DisplayName("게시글 사진 영속화에 실패하면 예외를 발생시킨다.")
    @Test
    void uploadPostImage_ImageUploadFail() throws IOException {
        Long memberId = 1L;
        MultipartFile multipartFile = generateTestImgMultipartFile();
        doThrow(new ImageUploadFailedException())
                .when(imageUploader)
                .upload(anyString(), any(InputStream.class));

        assertThatThrownBy(() -> postImageService.uploadPostImage(memberId, multipartFile))
                .isInstanceOf(ImageUploadFailedException.class);
    }
}
