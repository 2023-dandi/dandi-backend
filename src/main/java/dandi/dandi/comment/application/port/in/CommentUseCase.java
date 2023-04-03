package dandi.dandi.comment.application.port.in;

public interface CommentUseCase {

    void registerComment(Long memberId, Long postId, CommentRegisterCommand commentRegisterCommand);
}
