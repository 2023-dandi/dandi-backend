package dandi.dandi.comment.application;

import static dandi.dandi.comment.CommentFixture.COMMENT_CONTENT;
import static dandi.dandi.comment.CommentFixture.COMMENT_ID;
import static dandi.dandi.comment.CommentFixture.COMMENT_REGISTER_COMMAND;
import static dandi.dandi.member.MemberTestFixture.MEMBER;
import static dandi.dandi.member.MemberTestFixture.MEMBER2;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static dandi.dandi.post.PostFixture.TEST_POST;
import static dandi.dandi.utils.PaginationUtils.CREATED_AT_DESC_TEST_SIZE_PAGEABLE;
import static dandi.dandi.utils.TestImageUtils.IMAGE_ACCESS_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.comment.application.port.in.CommentResponses;
import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.comment.domain.Comment;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.event.notification.PostNotificationEvent;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private final CommentPersistencePort commentPersistencePort = Mockito.mock(CommentPersistencePort.class);
    private final PostPersistencePort postPersistencePort = Mockito.mock(PostPersistencePort.class);
    private final ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
    private final CommentService commentService =
            new CommentService(commentPersistencePort, postPersistencePort, applicationEventPublisher,
                    IMAGE_ACCESS_URL);

    @DisplayName("자신의 게시글에 댓글을 등록할 수 있다.")
    @Test
    void registerComment_MyPost() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(TEST_POST));
        when(commentPersistencePort.save(any(), any(), any()))
                .thenReturn(1L);

        commentService.registerComment(MEMBER_ID, POST_ID, COMMENT_REGISTER_COMMAND);

        assertAll(
                () -> verify(commentPersistencePort).save(any(), any(), any()),
                () -> verify(applicationEventPublisher, never()).publishEvent(any(PostNotificationEvent.class))
        );
    }

    @DisplayName("다른 사용자의 게시글에 댓글을 등록할 수 있다.")
    @Test
    void registerComment_OthersPost() {
        Long memberId = 2L;
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(TEST_POST));
        when(commentPersistencePort.save(any(), any(), any()))
                .thenReturn(1L);

        commentService.registerComment(memberId, POST_ID, COMMENT_REGISTER_COMMAND);

        assertAll(
                () -> verify(commentPersistencePort).save(any(), any(), any()),
                () -> verify(applicationEventPublisher).publishEvent(any(PostNotificationEvent.class))
        );
    }

    @DisplayName("존재하지 않는 게시글에 댓글을 등록하려하면 예외를 발생시킨다.")
    @Test
    void registerComment_NotFoundPost() {
        when(postPersistencePort.existsById(POST_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> commentService.registerComment(MEMBER_ID, POST_ID, COMMENT_REGISTER_COMMAND))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }

    @DisplayName("게시글의 댓글들을 조회할 수 있다.")
    @Test
    void getComments() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(TEST_POST));
        List<Comment> comments = List.of(
                new Comment(2L, "댓글 내용2", MEMBER, LocalDate.now()),
                new Comment(1L, "댓글 내용1", MEMBER2, LocalDate.now()));
        when(commentPersistencePort.findByPostId(POST_ID, CREATED_AT_DESC_TEST_SIZE_PAGEABLE))
                .thenReturn(new SliceImpl<>(comments, CREATED_AT_DESC_TEST_SIZE_PAGEABLE, false));

        CommentResponses commentResponses = commentService.getComments(MEMBER_ID, POST_ID,
                CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        assertAll(
                () -> assertThat(commentResponses.getComments().get(0).isPostWriter()).isTrue(),
                () -> assertThat(commentResponses.getComments().get(0).isMine()).isTrue(),
                () -> assertThat(commentResponses.getComments().get(1).isPostWriter()).isFalse(),
                () -> assertThat(commentResponses.getComments().get(1).isMine()).isFalse()
        );
    }

    @DisplayName("존재하지 않는 게시글의 댓글들을 조회하려하면 예외를 발생시킨다.")
    @Test
    void getComments_NotFoundPost() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.getComments(MEMBER_ID, POST_ID, CREATED_AT_DESC_TEST_SIZE_PAGEABLE))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }

    @DisplayName("자신이 작성한 댓글을 삭제할 수 있다.")
    @Test
    void deleteComment() {
        when(commentPersistencePort.findById(COMMENT_ID))
                .thenReturn(Optional.of(new Comment(COMMENT_ID, COMMENT_CONTENT, MEMBER, LocalDate.now())));

        commentService.deleteComment(MEMBER_ID, COMMENT_ID);

        verify(commentPersistencePort).deleteById(COMMENT_ID);
    }

    @DisplayName("존재하지 않는 댓글을 삭제하려하면 예외를 발생시킨다.")
    @Test
    void deleteComment_NotFound() {
        when(commentPersistencePort.findById(COMMENT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteComment(MEMBER_ID, COMMENT_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.comment().getMessage());
    }

    @DisplayName("다른 사용자의 댓글을 삭제하려하면 예외를 발생시킨다.")
    @Test
    void deleteComment_Forbidden() {
        when(commentPersistencePort.findById(COMMENT_ID))
                .thenReturn(Optional.of(new Comment(COMMENT_ID, COMMENT_CONTENT, MEMBER, LocalDate.now())));
        Long anotherMemberId = 2L;

        assertThatThrownBy(() -> commentService.deleteComment(anotherMemberId, COMMENT_ID))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ForbiddenException.commentDeletion().getMessage());
    }
}
