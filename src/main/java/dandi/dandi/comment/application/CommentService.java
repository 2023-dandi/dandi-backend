package dandi.dandi.comment.application;

import dandi.dandi.comment.application.port.in.CommentRegisterCommand;
import dandi.dandi.comment.application.port.in.CommentUseCase;
import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.comment.domain.Comment;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import org.springframework.stereotype.Service;

@Service
public class CommentService implements CommentUseCase {

    private final CommentPersistencePort commentPersistencePort;
    private final PostPersistencePort postPersistencePort;

    public CommentService(CommentPersistencePort commentPersistencePort, PostPersistencePort postPersistencePort) {
        this.commentPersistencePort = commentPersistencePort;
        this.postPersistencePort = postPersistencePort;
    }

    @Override
    public void registerComment(Long memberId, Long postId, CommentRegisterCommand commentRegisterCommand) {
        Comment comment = commentRegisterCommand.toComment();
        validatePostId(postId);
        commentPersistencePort.save(comment, postId, memberId);
    }

    private void validatePostId(Long postId) {
        if (!postPersistencePort.existsById(postId)) {
            throw NotFoundException.post();
        }
    }
}
