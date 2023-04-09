package dandi.dandi.comment.application.port.out;

public interface CommentReportPersistencePort {

    void saveReportOf(Long memberId, Long commentId);

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);
}
