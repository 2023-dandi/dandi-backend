package dandi.dandi.commentreport.adapter;

import dandi.dandi.commentreport.application.port.out.CommentReportPersistencePort;
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
}
