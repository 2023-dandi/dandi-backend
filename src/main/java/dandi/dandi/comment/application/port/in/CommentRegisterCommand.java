package dandi.dandi.comment.application.port.in;

import dandi.dandi.comment.domain.Comment;

public class CommentRegisterCommand {

    private Long postId;
    private String content;

    public CommentRegisterCommand() {
    }

    public CommentRegisterCommand(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }

    public Long getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }

    public Comment toComment() {
        return Comment.initial(content);
    }
}
