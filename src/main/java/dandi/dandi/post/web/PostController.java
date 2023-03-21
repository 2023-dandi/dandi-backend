package dandi.dandi.post.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostImageRegisterResponse;
import dandi.dandi.post.application.port.in.PostImageUseCase;
import dandi.dandi.post.application.port.in.PostRegisterResponse;
import dandi.dandi.post.application.port.in.PostUseCase;
import dandi.dandi.post.web.in.PostRegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<PostImageRegisterResponse> registerPostImage(@Login Long memberId,
                                                                       @RequestPart(value = "postImage")
                                                                       MultipartFile profileImage) {
        PostImageRegisterResponse postImageRegisterResponse = postImageUseCase.uploadPostImage(memberId, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(postImageRegisterResponse);
    }

    @PostMapping
    public ResponseEntity<PostRegisterResponse> registerPost(@Login Long memberId,
                                                             @RequestBody PostRegisterRequest postRegisterRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postUseCase.registerPost(memberId, postRegisterRequest.toCommand()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetails(@Login Long memberId, @PathVariable Long postId) {
        return ResponseEntity.ok(postUseCase.getPostDetails(memberId, postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@Login Long memberId, @PathVariable Long postId) {
        postUseCase.deletePost(memberId, postId);
        return ResponseEntity.noContent().build();
    }
}
