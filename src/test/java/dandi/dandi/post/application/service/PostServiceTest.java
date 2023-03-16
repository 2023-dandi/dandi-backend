package dandi.dandi.post.application.service;

import static dandi.dandi.post.PostFixture.ADDITIONAL_OUTFIT_FEELING_INDICES;
import static dandi.dandi.post.PostFixture.MAX_TEMPERATURE;
import static dandi.dandi.post.PostFixture.MIN_TEMPERATURE;
import static dandi.dandi.post.PostFixture.OUTFIT_FEELING_INDEX;
import static dandi.dandi.post.PostFixture.POST_IMAGE_URL;
import static dandi.dandi.post.PostFixture.TEST_POST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostRegisterCommand;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostPersistencePort postPersistencePort;

    @InjectMocks
    private PostService postService;

    @DisplayName("게시글을 작성할 수 있다.")
    @Test
    void registerPost() {
        Long memberId = 1L;
        PostRegisterCommand postRegisterCommand = new PostRegisterCommand(MIN_TEMPERATURE, MAX_TEMPERATURE,
                POST_IMAGE_URL, OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES);
        when(postPersistencePort.save(any(Post.class), any(Long.class)))
                .thenReturn(1L);

        Long postId = postService.registerPost(memberId, postRegisterCommand);

        assertThat(postId).isEqualTo(1L);
    }

    @DisplayName("게시글의 상세정보를 반환할 수 있다.")
    @Test
    void getPostDetails() {
        Long postId = 1L;
        when(postPersistencePort.findById(postId))
                .thenReturn(Optional.of(TEST_POST));

        PostDetailResponse postDetailsResponse = postService.getPostDetails(postId);

        assertAll(
                () -> assertThat(postDetailsResponse.getPostImageUrl())
                        .isEqualTo(TEST_POST.getPostImageUrl()),
                () -> assertThat(postDetailsResponse.getWriterNickname())
                        .isEqualTo(TEST_POST.getWriterNickname()),
                () -> assertThat(postDetailsResponse.getTemperatures().getMin())
                        .isEqualTo(TEST_POST.getMinTemperature()),
                () -> assertThat(postDetailsResponse.getTemperatures().getMax())
                        .isEqualTo(TEST_POST.getMaxTemperature()),
                () -> assertThat(postDetailsResponse.getOutfitFeelings().getFeelingIndex())
                        .isEqualTo(TEST_POST.getWeatherFeelingIndex()),
                () -> assertThat(postDetailsResponse.getOutfitFeelings().getAdditionalFeelingIndices())
                        .isEqualTo(TEST_POST.getAdditionalWeatherFeelingIndices())
        );
    }

    @DisplayName("상세 정보를 조회하려는 postId에 해당하는 게시글이 존재하지 않는다면 예외를 발생시킨다.")
    @Test
    void getPostDetails_PostNotFound() {
        Long postId = 1L;
        when(postPersistencePort.findById(postId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPostDetails(postId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }
}
