package dandi.dandi.comment.domain;

public class CommentCreatedEvent {

    private final Long targetMemberId;
    private final Long postId;
    private final Long commentId;

    public CommentCreatedEvent(Long targetMemberId, Long postId, Long commentId) {
        this.targetMemberId = targetMemberId;
        this.postId = postId;
        this.commentId = commentId;
    }

    public Long getTargetMemberId() {
        return targetMemberId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getCommentId() {
        return commentId;
    }
}
