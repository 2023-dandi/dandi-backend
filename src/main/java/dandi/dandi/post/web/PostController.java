package dandi.dandi.post.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.post.application.port.in.PostUseCase;
import dandi.dandi.post.web.in.PostRegisterRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController implements PostControllerDocs {

    private final PostUseCase postUseCase;

    public PostController(PostUseCase postUseCase) {
        this.postUseCase = postUseCase;
    }

    @PostMapping("/posts")
    public ResponseEntity<Void> registerPost(@Login Long memberId,
                                             @RequestBody PostRegisterRequest postRegisterRequest) {
        Long postId = postUseCase.registerPost(memberId, postRegisterRequest.toCommand());
        return ResponseEntity.created(URI.create("/posts/" + postId)).build();
    }
}
