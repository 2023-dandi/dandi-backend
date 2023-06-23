package dandi.dandi.post.application.service;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.ADDITIONAL_OUTFIT_FEELING_INDICES;
import static dandi.dandi.post.PostFixture.MAX_TEMPERATURE;
import static dandi.dandi.post.PostFixture.MIN_TEMPERATURE;
import static dandi.dandi.post.PostFixture.OUTFIT_FEELING_INDEX;
import static dandi.dandi.post.PostFixture.POST;
import static dandi.dandi.post.PostFixture.POST_IMAGE_FULL_URL;
import static dandi.dandi.utils.TestImageUtils.IMAGE_ACCESS_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.post.application.port.in.PostRegisterCommand;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostCommandServiceAdapterTest {

    private final PostPersistencePort postPersistencePort = Mockito.mock(PostPersistencePort.class);
    private final PostCommandServiceAdapter postService =
            new PostCommandServiceAdapter(postPersistencePort, IMAGE_ACCESS_URL);

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

    @DisplayName("자신이 작성한 게시글을 삭제할 수 있다.")
    @Test
    void deletePost() {
        Long postId = 1L;
        when(postPersistencePort.findById(postId))
                .thenReturn(Optional.of(POST));

        postService.deletePost(MEMBER_ID, postId);

        verify(postPersistencePort).deleteById(postId);
    }

    @DisplayName("자신이 작성하지 않은 게시글을 삭제하려고 할 시에 예외를 발생시킨다.")
    @Test
    void deletePost_Forbidden() {
        Long postId = 1L;
        Long postDeletionForbiddenMemberId = 2L;
        when(postPersistencePort.findById(postId))
                .thenReturn(Optional.of(POST));

        assertThatThrownBy(() -> postService.deletePost(postDeletionForbiddenMemberId, postId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ForbiddenException.postDeletion().getMessage());
    }
}
