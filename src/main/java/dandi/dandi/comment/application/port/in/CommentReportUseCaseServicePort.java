package dandi.dandi.comment.application.port.in;

public interface CommentReportUseCaseServicePort {

    void reportComment(Long memberId, Long commentId);
}
