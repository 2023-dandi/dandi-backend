package dandi.dandi.comment.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.comment.application.port.in.CommentQueryServicePort;
import dandi.dandi.comment.application.port.in.CommentResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentQueryController implements CommentQueryControllerDocs {

    private final CommentQueryServicePort commentQueryServicePort;

    public CommentQueryController(CommentQueryServicePort commentQueryServicePort) {
        this.commentQueryServicePort = commentQueryServicePort;
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponses> getComments(@Login Long memberId, @PathVariable Long postId,
                                                        Pageable pageable) {
        return ResponseEntity.ok(commentQueryServicePort.getComments(memberId, postId, pageable));
    }
}
