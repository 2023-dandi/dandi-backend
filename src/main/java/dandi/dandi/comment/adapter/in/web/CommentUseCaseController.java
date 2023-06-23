package dandi.dandi.comment.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.comment.application.port.in.CommentRegisterCommand;
import dandi.dandi.comment.application.port.in.CommentUseCaseServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentUseCaseController implements CommentUseCaseControllerDocs {

    private final CommentUseCaseServicePort commentUseCaseServicePort;

    public CommentUseCaseController(CommentUseCaseServicePort commentUseCaseServicePort) {
        this.commentUseCaseServicePort = commentUseCaseServicePort;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> registerComment(@Login Long memberId, @PathVariable Long postId,
                                                @RequestBody CommentRegisterCommand commentRegisterCommand) {
        commentUseCaseServicePort.registerComment(memberId, postId, commentRegisterCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@Login Long memberId, @PathVariable Long commentId) {
        commentUseCaseServicePort.deleteComment(memberId, commentId);
        return ResponseEntity.noContent().build();
    }
}
