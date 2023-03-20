package dandi.dandi.postlike.application.service;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.postlike.application.port.out.PostLikePersistencePort;
import dandi.dandi.postlike.domain.PostLike;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest {

    @Mock
    private PostPersistencePort postPersistencePort;

    @Mock
    private PostLikePersistencePort postLikePersistencePort;

    @InjectMocks
    private PostLikeService postLikeService;

    @DisplayName("좋아요를 누르지 않은 게시글에 좋아요를 등록할 수 있다.")
    @Test
    void reversePostLike_PostLikeRegister() {
        when(postPersistencePort.existsById(POST_ID))
                .thenReturn(true);
        when(postLikePersistencePort.findByMemberIdAndPostId(MEMBER_ID, POST_ID))
                .thenReturn(Optional.empty());

        postLikeService.reverseLike(MEMBER_ID, POST_ID);

        verify(postLikePersistencePort).save(any(PostLike.class));
    }

    @DisplayName("좋아요를 누른 게시글의 좋아요를 취소할 수 있다.")
    @Test
    void reversePostLike_PostLikeDeletion() {
        when(postPersistencePort.existsById(POST_ID))
                .thenReturn(true);
        PostLike postLike = new PostLike(1L, MEMBER_ID, POST_ID);
        when(postLikePersistencePort.findByMemberIdAndPostId(MEMBER_ID, POST_ID))
                .thenReturn(Optional.of(postLike));

        postLikeService.reverseLike(MEMBER_ID, POST_ID);

        verify(postLikePersistencePort).deleteById(postLike.getId());
    }

    @DisplayName("존재하지 않는 게시글에 좋아요를 reverse 하려고 하면 예외를 발생시킨다.")
    @Test
    void reversePostLike_NotFoundPost() {
        when(postPersistencePort.existsById(POST_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> postLikeService.reverseLike(MEMBER_ID, POST_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }
}