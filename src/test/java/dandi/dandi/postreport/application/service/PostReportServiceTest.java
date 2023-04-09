package dandi.dandi.postreport.application.service;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.postreport.application.port.out.PostReportPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostReportServiceTest {

    @Mock
    private PostPersistencePort postPersistencePort;

    @Mock
    private PostReportPersistencePort postReportPersistencePort;

    @InjectMocks
    private PostReportService postReportService;

    @DisplayName("존재하지 않는 글을 신고하려고 하면 예외를 발생시킨다.")
    @Test
    void reportPost_NotFound() {
        when(postPersistencePort.existsById(POST_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> postReportService.reportPost(MEMBER_ID, POST_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }

    @DisplayName("이미 신고한 글을 신고하려고 하면 예외를 발생시킨다.")
    @Test
    void reportPost_AlreadyReported() {
        when(postPersistencePort.existsById(POST_ID))
                .thenReturn(true);
        when(postReportPersistencePort.existsByMemberIdAndPostId(MEMBER_ID, POST_ID))
                .thenReturn(true);

        assertThatThrownBy(() -> postReportService.reportPost(MEMBER_ID, POST_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 신고한 게시글입니다.");
    }

    @DisplayName("게시글을 신고할 수 있다.")
    @Test
    void reportPost() {
        when(postPersistencePort.existsById(POST_ID))
                .thenReturn(true);
        when(postReportPersistencePort.existsByMemberIdAndPostId(MEMBER_ID, POST_ID))
                .thenReturn(false);

        postReportService.reportPost(MEMBER_ID, POST_ID);

        verify(postReportPersistencePort).savePostReportOf(MEMBER_ID, POST_ID);
    }
}
