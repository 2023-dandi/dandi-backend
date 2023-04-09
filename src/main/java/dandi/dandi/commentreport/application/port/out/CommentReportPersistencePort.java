package dandi.dandi.commentreport.application.port.out;

public interface CommentReportPersistencePort {

    void saveReportOf(Long memberId, Long commentId);

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);
}
