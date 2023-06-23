package dandi.dandi.postlike.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.postlike.application.port.in.PostLikeCommandServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostLikeCommandController implements PostLikeCommandControllerDocs {

    private final PostLikeCommandServicePort postLikeCommandServicePort;

    public PostLikeCommandController(PostLikeCommandServicePort postLikeCommandServicePort) {
        this.postLikeCommandServicePort = postLikeCommandServicePort;
    }

    @PatchMapping("/posts/{postId}/likes")
    public ResponseEntity<Void> reverseLikes(@Login Long memberId, @PathVariable Long postId) {
        postLikeCommandServicePort.reverseLike(memberId, postId);
        return ResponseEntity.noContent().build();
    }
}
