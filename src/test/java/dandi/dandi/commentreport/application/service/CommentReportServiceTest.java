package dandi.dandi.commentreport.application.service;

import static dandi.dandi.comment.CommentFixture.COMMENT_ID;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.common.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentReportServiceTest {

    @Mock
    private CommentPersistencePort commentPersistencePort;

    @InjectMocks
    private CommentReportService commentReportService;

    @DisplayName("존재하지 않는 댓글을 신고하려고 하면 예외를 발생시킨다.")
    @Test
    void reportComment_NotFound() {
        when(commentPersistencePort.existsById(COMMENT_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> commentReportService.reportComment(MEMBER_ID, COMMENT_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.comment().getMessage());
    }
}
