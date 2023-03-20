package dandi.dandi.post.application.service;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.member.MemberTestFixture.TEST_MEMBER;
import static dandi.dandi.post.PostFixture.ADDITIONAL_OUTFIT_FEELING_INDICES;
import static dandi.dandi.post.PostFixture.MAX_TEMPERATURE;
import static dandi.dandi.post.PostFixture.MIN_TEMPERATURE;
import static dandi.dandi.post.PostFixture.OUTFIT_FEELING_INDEX;
import static dandi.dandi.post.PostFixture.POST_IMAGE_FULL_URL;
import static dandi.dandi.post.PostFixture.TEST_POST;
import static dandi.dandi.utils.image.TestImageUtils.IMAGE_ACCESS_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostRegisterCommand;
import dandi.dandi.post.application.port.in.PostWriterResponse;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import dandi.dandi.postlike.application.port.out.PostLikePersistencePort;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private final PostPersistencePort postPersistencePort = Mockito.mock(PostPersistencePort.class);
    private final PostLikePersistencePort postLikePersistencePort = Mockito.mock(PostLikePersistencePort.class);
    private final PostService postService =
            new PostService(postPersistencePort, postLikePersistencePort, IMAGE_ACCESS_URL);

    @DisplayName("게시글을 작성할 수 있다.")
    @Test
    void registerPost() {
        Long memberId = 1L;
        PostRegisterCommand postRegisterCommand = new PostRegisterCommand(MIN_TEMPERATURE, MAX_TEMPERATURE,
                POST_IMAGE_FULL_URL, OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES);
        when(postPersistencePort.save(any(Post.class), any(Long.class)))
                .thenReturn(1L);

        Long postId = postService.registerPost(memberId, postRegisterCommand)
                .getPostId();

        assertThat(postId).isEqualTo(1L);
    }

    @DisplayName("게시글의 상세정보를 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void getPostDetails(Long memberId, boolean expectedMine) {
        Long postId = 1L;
        when(postPersistencePort.findById(postId))
                .thenReturn(Optional.of(TEST_POST));
        when(postLikePersistencePort.existsByPostIdAndMemberId(memberId, postId))
                .thenReturn(true);

        PostDetailResponse postDetailsResponse = postService.getPostDetails(memberId, postId);

        PostWriterResponse postWriterResponse = postDetailsResponse.getWriter();
        assertAll(
                () -> assertThat(postWriterResponse.getProfileImageUrl())
                        .startsWith(IMAGE_ACCESS_URL + TEST_MEMBER.getProfileImgUrl()),
                () -> assertThat(postWriterResponse.getId())
                        .isEqualTo(TEST_POST.getWriterId()),
                () -> assertThat(postWriterResponse.getNickname())
                        .isEqualTo(TEST_POST.getWriterNickname()),
                () -> assertThat(postDetailsResponse.isMine()).isEqualTo(expectedMine),
                () -> assertThat(postDetailsResponse.isLiked()).isTrue(),
                () -> assertThat(postDetailsResponse.getPostImageUrl())
                        .startsWith(IMAGE_ACCESS_URL + TEST_POST.getPostImageUrl()),
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

        assertThatThrownBy(() -> postService.getPostDetails(MEMBER_ID, postId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }

    @DisplayName("자신이 작성한 게시글을 삭제할 수 있다.")
    @Test
    void deletePost() {
        Long postId = 1L;
        when(postPersistencePort.findById(postId))
                .thenReturn(Optional.of(TEST_POST));

        postService.deletePost(MEMBER_ID, postId);

        verify(postPersistencePort).deleteById(postId);
    }

    @DisplayName("자신이 작성하지 않은 게시글을 삭제하려고 할 시에 예외를 발생시킨다.")
    @Test
    void deletePost_Forbidden() {
        Long postId = 1L;
        Long postDeletionForbiddenMemberId = 2L;
        when(postPersistencePort.findById(postId))
                .thenReturn(Optional.of(TEST_POST));

        assertThatThrownBy(() -> postService.deletePost(postDeletionForbiddenMemberId, postId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ForbiddenException.postDeletion().getMessage());
    }
}
