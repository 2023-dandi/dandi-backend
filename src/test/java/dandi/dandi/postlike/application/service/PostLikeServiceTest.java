package dandi.dandi.postlike.application.service;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static dandi.dandi.post.PostFixture.TEST_POST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.event.notification.PostNotificationEvent;
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
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest {

    @Mock
    private PostPersistencePort postPersistencePort;

    @Mock
    private PostLikePersistencePort postLikePersistencePort;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private PostLikeService postLikeService;

    @DisplayName("좋아요를 누르지 않은 자신의 게시글에 좋아요를 등록할 수 있다.")
    @Test
    void reversePostLike_PostLikeRegister_MyPost() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(TEST_POST));
        when(postLikePersistencePort.findByMemberIdAndPostId(MEMBER_ID, POST_ID))
                .thenReturn(Optional.empty());

        postLikeService.reverseLike(MEMBER_ID, POST_ID);

        assertAll(
                () -> verify(postLikePersistencePort).save(any(PostLike.class)),
                () -> verify(applicationEventPublisher, never()).publishEvent(any(PostNotificationEvent.class))
        );
    }

    @DisplayName("좋아요를 누르지 않은 다른 사용자의 게시글에 좋아요를 등록할 수 있다.")
    @Test
    void reversePostLike_PostLikeRegister_OthersPost() {
        Long memberId = 2L;
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(TEST_POST));
        when(postLikePersistencePort.findByMemberIdAndPostId(memberId, POST_ID))
                .thenReturn(Optional.empty());

        postLikeService.reverseLike(memberId, POST_ID);

        assertAll(
                () -> verify(postLikePersistencePort).save(any(PostLike.class)),
                () -> verify(applicationEventPublisher).publishEvent(any(PostNotificationEvent.class))
        );
    }

    @DisplayName("좋아요를 누른 게시글의 좋아요를 취소할 수 있다.")
    @Test
    void reversePostLike_PostLikeDeletion() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(TEST_POST));
        PostLike postLike = new PostLike(1L, MEMBER_ID, POST_ID);
        when(postLikePersistencePort.findByMemberIdAndPostId(MEMBER_ID, POST_ID))
                .thenReturn(Optional.of(postLike));

        postLikeService.reverseLike(MEMBER_ID, POST_ID);

        verify(postLikePersistencePort).deleteById(postLike.getId());
    }

    @DisplayName("존재하지 않는 게시글에 좋아요를 reverse 하려고 하면 예외를 발생시킨다.")
    @Test
    void reversePostLike_NotFoundPost() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> postLikeService.reverseLike(MEMBER_ID, POST_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }
}
