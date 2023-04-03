package dandi.dandi.comment;

import dandi.dandi.comment.application.port.in.CommentRegisterCommand;

public class CommentFixture {

    public static final String COMMENT_CONTENT = "댓글 내용";
    public static final CommentRegisterCommand COMMENT_REGISTER_COMMAND = new CommentRegisterCommand(COMMENT_CONTENT);
}
