package dandi.dandi.commentreport.application.port.in;

public interface CommentReportUseCase {

    void reportComment(Long memberId, Long commentId);
}
