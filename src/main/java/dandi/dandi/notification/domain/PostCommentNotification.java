package dandi.dandi.notification.domain;

import static dandi.dandi.notification.domain.NotificationType.COMMENT;

import java.time.LocalDate;

public class PostCommentNotification extends PostNotification {

    private final Long commentId;
    private final String commentContent;

    public PostCommentNotification(Long id, Long memberId, NotificationType type, LocalDate createdAt, Long postId,
                                   Long commentId, String commentContent) {
        super(id, memberId, type, createdAt, postId);
        this.commentId = commentId;
        this.commentContent = commentContent;
    }

    public static PostCommentNotification initial(Long memberId, Long postId, Long commentId) {
        return new PostCommentNotification(null, memberId, COMMENT, null, postId, commentId, null);
    }

    @Override
    public final Long getCommentId() {
        return commentId;
    }

    @Override
    public final String getCommentContent() {
        return commentContent;
    }
}
