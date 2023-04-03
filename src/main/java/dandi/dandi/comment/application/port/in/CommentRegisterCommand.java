package dandi.dandi.comment.application.port.in;

import dandi.dandi.comment.domain.Comment;

public class CommentRegisterCommand {

    private String content;

    public CommentRegisterCommand() {
    }

    public CommentRegisterCommand(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Comment toComment() {
        return Comment.initial(content);
    }
}
