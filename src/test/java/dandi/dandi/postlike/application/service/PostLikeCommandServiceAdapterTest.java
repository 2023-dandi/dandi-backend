package dandi.dandi.postlike.application.service;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.event.application.port.out.EventPort;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.postlike.application.port.out.PostLikePersistencePort;
import dandi.dandi.postlike.domain.PostLike;
import dandi.dandi.postlike.domain.PostLikedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostLikeCommandServiceAdapterTest {

    @Mock
    private PostPersistencePort postPersistencePort;

    @Mock
    private PostLikePersistencePort postLikePersistencePort;

    @Mock
    private EventPort eventPort;

    @InjectMocks
    private PostLikeCommandServiceAdapter postLikeCommandServiceAdapter;

    @DisplayName("좋아요를 누르지 않은 자신의 게시글에 좋아요를 등록할 수 있다.")
    @Test
    void reversePostLike_PostLikeRegister_MyPost() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(POST));
        when(postLikePersistencePort.findByMemberIdAndPostId(MEMBER_ID, POST_ID))
                .thenReturn(Optional.empty());

        postLikeCommandServiceAdapter.reverseLike(MEMBER_ID, POST_ID);

        assertAll(
                () -> verify(postLikePersistencePort).save(any(PostLike.class)),
                () -> verify(eventPort, never()).publishEvent(any(PostLikedEvent.class))
        );
    }

    @DisplayName("좋아요를 누르지 않은 다른 사용자의 게시글에 좋아요를 등록할 수 있다.")
    @Test
    void reversePostLike_PostLikeRegister_OthersPost() {
        Long memberId = 2L;
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(POST));
        when(postLikePersistencePort.findByMemberIdAndPostId(memberId, POST_ID))
                .thenReturn(Optional.empty());

        postLikeCommandServiceAdapter.reverseLike(memberId, POST_ID);

        assertAll(
                () -> verify(postLikePersistencePort).save(any(PostLike.class)),
                () -> verify(eventPort).publishEvent(any(PostLikedEvent.class))
        );
    }

    @DisplayName("좋아요를 누른 게시글의 좋아요를 취소할 수 있다.")
    @Test
    void reversePostLike_PostLikeDeletion() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(POST));
        PostLike postLike = new PostLike(MEMBER_ID, POST_ID);
        when(postLikePersistencePort.findByMemberIdAndPostId(MEMBER_ID, POST_ID))
                .thenReturn(Optional.of(postLike));

        postLikeCommandServiceAdapter.reverseLike(MEMBER_ID, POST_ID);

        verify(postLikePersistencePort).deleteByPostIdAndMemberId(POST_ID, MEMBER_ID);
    }

    @DisplayName("존재하지 않는 게시글에 좋아요를 reverse 하려고 하면 예외를 발생시킨다.")
    @Test
    void reversePostLike_NotFoundPost() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> postLikeCommandServiceAdapter.reverseLike(MEMBER_ID, POST_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }
}
