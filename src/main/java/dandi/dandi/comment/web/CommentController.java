package dandi.dandi.comment.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.comment.application.port.in.CommentRegisterCommand;
import dandi.dandi.comment.application.port.in.CommentUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController implements CommentControllerDocs {

    private final CommentUseCase commentUseCase;

    public CommentController(CommentUseCase commentUseCase) {
        this.commentUseCase = commentUseCase;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> registerComment(@Login Long memberId, @PathVariable Long postId,
                                                @RequestBody CommentRegisterCommand commentRegisterCommand) {
        commentUseCase.registerComment(memberId, postId, commentRegisterCommand);
        return ResponseEntity.noContent().build();
    }
}
