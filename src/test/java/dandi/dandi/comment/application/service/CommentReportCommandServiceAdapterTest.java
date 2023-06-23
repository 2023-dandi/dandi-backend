package dandi.dandi.comment.application.service;

import static dandi.dandi.comment.CommentFixture.COMMENT_ID;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.comment.application.port.out.CommentReportPersistencePort;
import dandi.dandi.common.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentReportCommandServiceAdapterTest {

    @Mock
    private CommentPersistencePort commentPersistencePort;
    @Mock
    private CommentReportPersistencePort commentReportPersistencePort;
    @InjectMocks
    private CommentReportCommandServiceAdapter commentReportCommandServiceAdapter;

    @DisplayName("존재하지 않는 댓글을 신고하려고 하면 예외를 발생시킨다.")
    @Test
    void reportComment_NotFound() {
        when(commentPersistencePort.existsById(COMMENT_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> commentReportCommandServiceAdapter.reportComment(MEMBER_ID, COMMENT_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.comment().getMessage());
    }

    @DisplayName("이미 신고한 댓글을 신고하려 하면 예외를 발생시킨다.")
    @Test
    void reportComment_AlreadyReported() {
        when(commentPersistencePort.existsById(COMMENT_ID))
                .thenReturn(true);
        when(commentReportPersistencePort.existsByMemberIdAndCommentId(MEMBER_ID, COMMENT_ID))
                .thenReturn(true);

        assertThatThrownBy(() -> commentReportCommandServiceAdapter.reportComment(MEMBER_ID, COMMENT_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 신고한 댓글입니다.");
    }

    @DisplayName("댓글을 신고할 수 있다.")
    @Test
    void reportComment() {
        when(commentPersistencePort.existsById(COMMENT_ID))
                .thenReturn(true);
        when(commentReportPersistencePort.existsByMemberIdAndCommentId(MEMBER_ID, COMMENT_ID))
                .thenReturn(false);

        commentReportCommandServiceAdapter.reportComment(MEMBER_ID, COMMENT_ID);

        verify(commentReportPersistencePort).saveReportOf(MEMBER_ID, COMMENT_ID);
    }
}
