package dandi.dandi.comment.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.comment.application.port.in.CommentRegisterCommand;
import dandi.dandi.comment.application.port.in.CommentResponses;
import dandi.dandi.comment.application.port.in.CommentUseCase;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponses> getComments(@Login Long memberId, @PathVariable Long postId,
                                                        Pageable pageable) {
        return ResponseEntity.ok(commentUseCase.getComments(memberId, postId, pageable));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@Login Long memberId, @PathVariable Long commentId) {
        commentUseCase.deleteComment(memberId, commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/comments/{commentId}/reports")
    public ResponseEntity<Void> reportComment(@Login Long memberId, @PathVariable Long commentId) {
        commentUseCase.reportComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
