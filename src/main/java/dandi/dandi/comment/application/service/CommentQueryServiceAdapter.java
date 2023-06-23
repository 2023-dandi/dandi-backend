package dandi.dandi.comment.application.service;

import dandi.dandi.comment.application.port.in.CommentQueryServicePort;
import dandi.dandi.comment.application.port.in.CommentResponse;
import dandi.dandi.comment.application.port.in.CommentResponses;
import dandi.dandi.comment.application.port.in.CommentWriterResponse;
import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.comment.domain.Comment;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentQueryServiceAdapter implements CommentQueryServicePort {

    private final PostPersistencePort postPersistencePort;
    private final CommentPersistencePort commentPersistencePort;
    private final String imageAccessUrl;

    public CommentQueryServiceAdapter(PostPersistencePort postPersistencePort,
                                      CommentPersistencePort commentPersistencePort,
                                      @Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl) {
        this.postPersistencePort = postPersistencePort;
        this.commentPersistencePort = commentPersistencePort;
        this.imageAccessUrl = imageAccessUrl;
    }

    @Override
    public CommentResponses getComments(Long memberId, Long postId, Pageable pageable) {
        Post post = postPersistencePort.findById(postId)
                .orElseThrow(NotFoundException::post);
        Slice<Comment> comments = commentPersistencePort.findByPostId(memberId, postId, pageable);
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
}
