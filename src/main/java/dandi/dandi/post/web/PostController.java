package dandi.dandi.post.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.post.application.port.in.PostImageUseCase;
import dandi.dandi.post.application.port.in.PostUseCase;
import dandi.dandi.post.web.in.PostRegisterRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
public class PostController implements PostControllerDocs {

    private final PostUseCase postUseCase;
    private final PostImageUseCase postImageUseCase;

    public PostController(PostUseCase postUseCase, PostImageUseCase postImageUseCase) {
        this.postUseCase = postUseCase;
        this.postImageUseCase = postImageUseCase;
    }

    @PostMapping("/images")
    public ResponseEntity<Void> registerPostImage(@Login Long memberId,
                                                  @RequestPart(value = "postImage")
                                                  MultipartFile profileImage) {
        String postImageUrl = postImageUseCase.uploadPostImage(memberId, profileImage);
        return ResponseEntity.created(URI.create(postImageUrl)).build();
    }

    @PostMapping
    public ResponseEntity<Void> registerPost(@Login Long memberId,
                                             @RequestBody PostRegisterRequest postRegisterRequest) {
        Long postId = postUseCase.registerPost(memberId, postRegisterRequest.toCommand());
        return ResponseEntity.created(URI.create("/posts/" + postId)).build();
    }
}
