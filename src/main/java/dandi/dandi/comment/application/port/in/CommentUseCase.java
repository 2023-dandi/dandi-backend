package dandi.dandi.comment.application.port.in;

import org.springframework.data.domain.Pageable;

public interface CommentUseCase {

    void registerComment(Long memberId, Long postId, CommentRegisterCommand commentRegisterCommand);

    CommentResponses getComments(Long memberId, Long postId, Pageable pageable);

    void deleteComment(Long memberId, Long commentId);
}
