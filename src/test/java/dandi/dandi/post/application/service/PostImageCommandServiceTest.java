package dandi.dandi.post.application.service;

import static dandi.dandi.post.PostFixture.POST_IMAGE_DIR;
import static dandi.dandi.utils.TestImageUtils.generateTestImgMultipartFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import dandi.dandi.image.application.out.ImageManager;
import dandi.dandi.image.application.out.UnusedImagePersistencePort;
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
class PostImageCommandServiceTest {

    private final ImageManager imageManager = Mockito.mock(ImageManager.class);
    private final UnusedImagePersistencePort unusedImagePersistencePort =
            Mockito.mock(UnusedImagePersistencePort.class);
    private final PostImageCommandService postImageCommandService =
            new PostImageCommandService(imageManager, unusedImagePersistencePort, POST_IMAGE_DIR);

    @DisplayName("게시글 사진을 업로드할 수 있다.")
    @Test
    void uploadPostImage() throws IOException {
        Long memberId = 1L;
        MultipartFile multipartFile = generateTestImgMultipartFile();

        PostImageRegisterResponse postImageRegisterResponse = postImageCommandService.uploadPostImage(memberId,
                multipartFile);

        assertAll(
                () -> verify(imageManager).upload(anyString(), any(InputStream.class)),
                () -> verify(unusedImagePersistencePort).save(anyString()),
                () -> assertThat(postImageRegisterResponse.getPostImageUrl())
                        .startsWith(POST_IMAGE_DIR)
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
                .when(imageManager)
                .upload(anyString(), any(InputStream.class));

        assertAll(
                () -> verify(unusedImagePersistencePort, never()).save(POST_IMAGE_DIR),
                () -> assertThatThrownBy(() -> postImageCommandService.uploadPostImage(memberId, multipartFile))
                        .isInstanceOf(ImageUploadFailedException.class)
        );
    }
}
