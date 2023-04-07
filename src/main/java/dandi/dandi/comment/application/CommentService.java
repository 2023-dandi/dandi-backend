package dandi.dandi.comment.application;

import dandi.dandi.comment.application.port.in.CommentRegisterCommand;
import dandi.dandi.comment.application.port.in.CommentResponse;
import dandi.dandi.comment.application.port.in.CommentResponses;
import dandi.dandi.comment.application.port.in.CommentUseCase;
import dandi.dandi.comment.application.port.in.CommentWriterResponse;
import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.comment.domain.Comment;
import dandi.dandi.comment.domain.CommentCreatedEvent;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService implements CommentUseCase {

    private final CommentPersistencePort commentPersistencePort;
    private final PostPersistencePort postPersistencePort;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final String imageAccessUrl;

    public CommentService(CommentPersistencePort commentPersistencePort, PostPersistencePort postPersistencePort,
                          ApplicationEventPublisher applicationEventPublisher,
                          @Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl) {
        this.commentPersistencePort = commentPersistencePort;
        this.postPersistencePort = postPersistencePort;
        this.applicationEventPublisher = applicationEventPublisher;
        this.imageAccessUrl = imageAccessUrl;
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
            applicationEventPublisher.publishEvent(
                    new CommentCreatedEvent(post.getWriterId(), post.getId(), commentId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponses getComments(Long memberId, Long postId, Pageable pageable) {
        Post post = postPersistencePort.findById(postId)
                .orElseThrow(NotFoundException::post);
        Slice<Comment> comments = commentPersistencePort.findByPostId(postId, pageable);
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> mapToCommentResponse(comment, post, memberId))
                .collect(Collectors.toUnmodifiableList());
        return new CommentResponses(commentResponses, comments.isLast());
    }

    private CommentResponse mapToCommentResponse(Comment comment, Post post, Long memberId) {
        return new CommentResponse(
                comment.getId(),
                new CommentWriterResponse(comment.getWriter(), imageAccessUrl),
                comment.isWittenBy(post.getWriterId()),
                comment.isWittenBy(memberId),
                comment.getCreatedAt(),
                comment.getContent()
        );
    }

    @Override
    @Transactional
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
