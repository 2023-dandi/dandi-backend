package dandi.dandi.notification.domain;

import java.time.LocalDateTime;

public class PostCommentNotification extends PostNotification {

    private final Long commentId;
    private final String commentContent;

    public PostCommentNotification(Long id, Long memberId, NotificationType type, Long postId,
                                   LocalDateTime createdAt, Long commentId, String commentContent) {
        super(id, memberId, type, postId, createdAt);
        this.commentId = commentId;
        this.commentContent = commentContent;
    }

    public static PostCommentNotification initial(Long memberId, Long postId, Long commentId) {
        return new PostCommentNotification(null, memberId, NotificationType.COMMENT, postId, null, commentId, null);
    }
}
