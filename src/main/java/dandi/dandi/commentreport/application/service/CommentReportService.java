package dandi.dandi.commentreport.application.service;

import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.commentreport.application.port.in.CommentReportUseCase;
import dandi.dandi.commentreport.application.port.out.CommentReportPersistencePort;
import dandi.dandi.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentReportService implements CommentReportUseCase {

    private final CommentPersistencePort commentPersistencePort;
    private final CommentReportPersistencePort commentReportPersistencePort;

    public CommentReportService(CommentPersistencePort commentPersistencePort,
                                CommentReportPersistencePort commentReportPersistencePort) {
        this.commentPersistencePort = commentPersistencePort;
        this.commentReportPersistencePort = commentReportPersistencePort;
    }

    @Override
    @Transactional
    public void reportComment(Long memberId, Long commentId) {
        validateCommentExistence(commentId);
        validateAlreadyReported(memberId, commentId);
        commentReportPersistencePort.saveReportOf(memberId, commentId);
    }

    private void validateCommentExistence(Long commentId) {
        if (!commentPersistencePort.existsById(commentId)) {
            throw NotFoundException.comment();
        }
    }

    private void validateAlreadyReported(Long memberId, Long commentId) {
        if (commentReportPersistencePort.existsByMemberIdAndCommentId(memberId, commentId)) {
            throw new IllegalStateException("이미 신고한 댓글입니다.");
        }
    }
}
