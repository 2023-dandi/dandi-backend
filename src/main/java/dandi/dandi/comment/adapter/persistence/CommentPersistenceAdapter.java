package dandi.dandi.comment.adapter.persistence;

import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.comment.domain.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentPersistenceAdapter implements CommentPersistencePort {

    private final CommentRepository commentRepository;

    public CommentPersistenceAdapter(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void save(Comment comment, Long postId, Long memberId) {
        CommentJpaEntity commentJpaEntity = CommentJpaEntity.of(comment, postId, memberId);
        commentRepository.save(commentJpaEntity);
    }
}
