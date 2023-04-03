package dandi.dandi.comment.application.port.in;

import dandi.dandi.comment.application.port.in.CommentRegisterCommand;

public interface CommentUseCase {

    void registerComment(Long memberId, CommentRegisterCommand commentRegisterCommand);
}
