package dandi.dandi.comment.application.service;

import dandi.dandi.comment.application.port.in.CommentRegisterCommand;
import dandi.dandi.comment.application.port.in.CommentUseCaseServicePort;
import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.comment.domain.Comment;
import dandi.dandi.comment.domain.CommentCreatedEvent;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.event.application.port.out.EventPort;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentCommandServiceAdapter implements CommentUseCaseServicePort {

    private final CommentPersistencePort commentPersistencePort;
    private final PostPersistencePort postPersistencePort;
    private final EventPort eventPort;

    public CommentCommandServiceAdapter(CommentPersistencePort commentPersistencePort,
                                        PostPersistencePort postPersistencePort,
                                        EventPort eventPort) {
        this.commentPersistencePort = commentPersistencePort;
        this.postPersistencePort = postPersistencePort;
        this.eventPort = eventPort;
    }

    @Override
    public void registerComment(Long memberId, Long postId, CommentRegisterCommand commentRegisterCommand) {
        Comment comment = commentRegisterCommand.toComment();
        Post post = postPersistencePort.findById(postId)
                .orElseThrow(NotFoundException::post);
        Long commentId = commentPersistencePort.save(comment, postId, memberId);
        publishPostNotificationIfNotifiable(memberId, post, commentId);
    }

    private void publishPostNotificationIfNotifiable(Long memberId, Post post, Long commentId) {
        if (!post.isWrittenBy(memberId)) {
            eventPort.publishEvent(
                    new CommentCreatedEvent(post.getWriterId(), post.getId(), commentId));
        }
    }

    @Override
    public void deleteComment(Long memberId, Long commentId) {
        Comment comment = commentPersistencePort.findById(commentId)
                .orElseThrow(NotFoundException::comment);
        validateOwner(memberId, comment);
        commentPersistencePort.deleteById(commentId);
    }

    private void validateOwner(Long memberId, Comment comment) {
        if (!comment.isWittenBy(memberId)) {
            throw ForbiddenException.commentDeletion();
        }
    }
}
