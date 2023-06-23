package dandi.dandi.comment.application.port.in;

public interface CommentUseCaseServicePort {

    void registerComment(Long memberId, Long postId, CommentRegisterCommand commentRegisterCommand);

    void deleteComment(Long memberId, Long commentId);
}
