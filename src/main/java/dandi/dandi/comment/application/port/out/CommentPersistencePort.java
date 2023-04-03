package dandi.dandi.comment.application.port.out;

import dandi.dandi.comment.domain.Comment;

public interface CommentPersistencePort {

    void save(Comment comment, Long postId, Long memberId);
}
