package dandi.dandi.commentreport.application.service;

import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.commentreport.application.port.in.CommentReportUseCase;
import dandi.dandi.common.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class CommentReportService implements CommentReportUseCase {

    private final CommentPersistencePort commentPersistencePort;

    public CommentReportService(CommentPersistencePort commentPersistencePort) {
        this.commentPersistencePort = commentPersistencePort;
    }

    @Override
    @Transactional
    public void reportComment(Long memberId, Long commentId) {
        validateCommentExistence(commentId);
    }

    private void validateCommentExistence(Long commentId) {
        if (!commentPersistencePort.existsById(commentId)) {
            throw NotFoundException.comment();
        }
    }
}
