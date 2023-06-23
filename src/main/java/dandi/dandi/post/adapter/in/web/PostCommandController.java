package dandi.dandi.post.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.post.adapter.in.web.dto.PostRegisterRequest;
import dandi.dandi.post.application.port.in.PostCommandServicePort;
import dandi.dandi.post.application.port.in.PostRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostCommandController implements PostCommandControllerDocs {

    private final PostCommandServicePort postUseCaseServicePort;

    public PostCommandController(PostCommandServicePort postUseCaseServicePort) {
        this.postUseCaseServicePort = postUseCaseServicePort;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostRegisterResponse> registerPost(@Login Long memberId,
                                                             @RequestBody PostRegisterRequest postRegisterRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postUseCaseServicePort.registerPost(memberId, postRegisterRequest.toCommand()));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@Login Long memberId, @PathVariable Long postId) {
        postUseCaseServicePort.deletePost(memberId, postId);
        return ResponseEntity.noContent().build();
    }
}
