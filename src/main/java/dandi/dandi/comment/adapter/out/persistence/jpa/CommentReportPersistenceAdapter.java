package dandi.dandi.comment.adapter.out.persistence.jpa;

import dandi.dandi.comment.application.port.out.CommentReportPersistencePort;
import org.springframework.stereotype.Component;

@Component
public class CommentReportPersistenceAdapter implements CommentReportPersistencePort {

    private final CommentReportRepository commentReportRepository;

    public CommentReportPersistenceAdapter(CommentReportRepository commentReportRepository) {
        this.commentReportRepository = commentReportRepository;
    }

    @Override
    public void saveReportOf(Long memberId, Long commentId) {
        CommentReportJpaEntity commentReportJpaEntity = new CommentReportJpaEntity(memberId, commentId);
        commentReportRepository.save(commentReportJpaEntity);
    }

    @Override
    public boolean existsByMemberIdAndCommentId(Long memberId, Long commentId) {
        return commentReportRepository.existsByMemberIdAndCommentId(memberId, commentId);
    }
}
