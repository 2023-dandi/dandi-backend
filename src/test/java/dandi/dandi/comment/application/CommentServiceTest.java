package dandi.dandi.comment.application;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.comment.application.port.in.CommentRegisterCommand;
import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentPersistencePort commentPersistencePort;

    @Mock
    private PostPersistencePort postPersistencePort;

    @InjectMocks
    private CommentService commentService;

    @DisplayName("댓글을 저장할 수 있다.")
    @Test
    void registerComment() {
        CommentRegisterCommand commentRegisterCommand = new CommentRegisterCommand("댓글 내용");
        when(postPersistencePort.existsById(POST_ID))
                .thenReturn(true);

        commentService.registerComment(MEMBER_ID, POST_ID, commentRegisterCommand);

        verify(commentPersistencePort).save(any(), any(), any());
    }

    @DisplayName("존재하지 않는 게시글에 댓글을 등록하려하면 예외를 발생시킨다.")
    @Test
    void registerComment_NotFoundPost() {
        CommentRegisterCommand commentRegisterCommand = new CommentRegisterCommand("댓글 내용");
        when(postPersistencePort.existsById(POST_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> commentService.registerComment(MEMBER_ID, POST_ID, commentRegisterCommand))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }
}