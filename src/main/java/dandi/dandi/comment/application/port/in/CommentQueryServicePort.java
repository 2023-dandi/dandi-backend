package dandi.dandi.comment.application.port.in;

import org.springframework.data.domain.Pageable;

public interface CommentQueryServicePort {

    CommentResponses getComments(Long memberId, Long postId, Pageable pageable);
}
