package dandi.dandi.postlike.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.postlike.application.port.in.PostLikeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostLikeController implements PostLikeControllerDocs {

    private final PostLikeUseCase postLikeUseCase;

    public PostLikeController(PostLikeUseCase postLikeUseCase) {
        this.postLikeUseCase = postLikeUseCase;
    }

    @PatchMapping("/posts/{postId}/likes")
    public ResponseEntity<Void> reverseLikes(@Login Long memberId, @PathVariable Long postId) {
        postLikeUseCase.reverseLike(memberId, postId);
        return ResponseEntity.noContent().build();
    }
}
