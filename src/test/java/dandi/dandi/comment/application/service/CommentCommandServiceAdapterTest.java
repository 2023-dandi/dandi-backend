package dandi.dandi.comment.application.service;

import static dandi.dandi.comment.CommentFixture.COMMENT_CONTENT;
import static dandi.dandi.comment.CommentFixture.COMMENT_ID;
import static dandi.dandi.comment.CommentFixture.COMMENT_REGISTER_COMMAND;
import static dandi.dandi.member.MemberTestFixture.MEMBER;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.comment.domain.Comment;
import dandi.dandi.comment.domain.CommentCreatedEvent;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.event.application.port.out.EventPort;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentCommandServiceAdapterTest {

    @Mock
    private CommentPersistencePort commentPersistencePort;
    @Mock
    private PostPersistencePort postPersistencePort;
    @Mock
    private EventPort eventPort;
    @InjectMocks
    private CommentCommandServiceAdapter commentCommandServiceAdapter;

    @DisplayName("자신의 게시글에 댓글을 등록할 수 있다.")
    @Test
    void registerComment_MyPost() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(POST));
        when(commentPersistencePort.save(any(), any(), any()))
                .thenReturn(1L);

        commentCommandServiceAdapter.registerComment(MEMBER_ID, POST_ID, COMMENT_REGISTER_COMMAND);

        assertAll(
                () -> verify(commentPersistencePort).save(any(), any(), any()),
                () -> verify(eventPort, never()).publishEvent(any(CommentCreatedEvent.class))
        );
    }

    @DisplayName("다른 사용자의 게시글에 댓글을 등록할 수 있다.")
    @Test
    void registerComment_OthersPost() {
        Long memberId = 2L;
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(POST));
        when(commentPersistencePort.save(any(), any(), any()))
                .thenReturn(1L);

        commentCommandServiceAdapter.registerComment(memberId, POST_ID, COMMENT_REGISTER_COMMAND);

        assertAll(
                () -> verify(commentPersistencePort).save(any(), any(), any()),
                () -> verify(eventPort).publishEvent(any(CommentCreatedEvent.class))
        );
    }

    @DisplayName("존재하지 않는 게시글에 댓글을 등록하려하면 예외를 발생시킨다.")
    @Test
    void registerComment_NotFoundPost() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> commentCommandServiceAdapter.registerComment(MEMBER_ID, POST_ID, COMMENT_REGISTER_COMMAND))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }

    @DisplayName("자신이 작성한 댓글을 삭제할 수 있다.")
    @Test
    void deleteComment() {
        when(commentPersistencePort.findById(COMMENT_ID))
                .thenReturn(Optional.of(new Comment(COMMENT_ID, COMMENT_CONTENT, MEMBER, LocalDate.now())));

        commentCommandServiceAdapter.deleteComment(MEMBER_ID, COMMENT_ID);

        verify(commentPersistencePort).deleteById(COMMENT_ID);
    }

    @DisplayName("존재하지 않는 댓글을 삭제하려하면 예외를 발생시킨다.")
    @Test
    void deleteComment_NotFound() {
        when(commentPersistencePort.findById(COMMENT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentCommandServiceAdapter.deleteComment(MEMBER_ID, COMMENT_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.comment().getMessage());
    }

    @DisplayName("다른 사용자의 댓글을 삭제하려하면 예외를 발생시킨다.")
    @Test
    void deleteComment_Forbidden() {
        when(commentPersistencePort.findById(COMMENT_ID))
                .thenReturn(Optional.of(new Comment(COMMENT_ID, COMMENT_CONTENT, MEMBER, LocalDate.now())));
        Long anotherMemberId = 2L;

        assertThatThrownBy(() -> commentCommandServiceAdapter.deleteComment(anotherMemberId, COMMENT_ID))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ForbiddenException.commentDeletion().getMessage());
    }
}
