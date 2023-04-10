package dandi.dandi.post.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.post.application.port.in.FeedResponse;
import dandi.dandi.post.application.port.in.LikedPostResponses;
import dandi.dandi.post.application.port.in.MyPostResponses;
import dandi.dandi.post.application.port.in.MyPostsByTemperatureResponses;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostImageRegisterResponse;
import dandi.dandi.post.application.port.in.PostImageUseCase;
import dandi.dandi.post.application.port.in.PostRegisterResponse;
import dandi.dandi.post.application.port.in.PostUseCase;
import dandi.dandi.post.web.in.PostRegisterRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/posts/images")
    public ResponseEntity<PostImageRegisterResponse> registerPostImage(@Login Long memberId,
                                                                       @RequestPart(value = "postImage")
                                                                       MultipartFile profileImage) {
        PostImageRegisterResponse postImageRegisterResponse = postImageUseCase.uploadPostImage(memberId, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(postImageRegisterResponse);
    }

    @PostMapping("/posts")
    public ResponseEntity<PostRegisterResponse> registerPost(@Login Long memberId,
                                                             @RequestBody PostRegisterRequest postRegisterRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postUseCase.registerPost(memberId, postRegisterRequest.toCommand()));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetails(@Login Long memberId, @PathVariable Long postId) {
        return ResponseEntity.ok(postUseCase.getPostDetails(memberId, postId));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@Login Long memberId, @PathVariable Long postId) {
        postUseCase.deletePost(memberId, postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/posts/my")
    public ResponseEntity<MyPostResponses> getMyPostIdsAndPostImageUrls(@Login Long memberId,
                                                                        @PageableDefault(size = 500, sort = "createdAt",
                                                                                direction = Direction.DESC)
                                                                        Pageable pageable) {
        return ResponseEntity.ok(postUseCase.getMyPostIdsAndPostImageUrls(memberId, pageable));
    }

    @GetMapping("/posts/feed/temperature")
    public ResponseEntity<FeedResponse> getFeedsByTemperature(@Login Long memberId, Pageable pageable,
                                                              @RequestParam(value = "min") Double minTemperature,
                                                              @RequestParam(value = "max") Double maxTemperature) {
        return ResponseEntity.ok(postUseCase.getPostsByTemperature(memberId, minTemperature, maxTemperature, pageable));
    }

    @GetMapping("/posts/my/temperature")
    public ResponseEntity<MyPostsByTemperatureResponses> getMyPostsByTemperature(@Login Long memberId,
                                                                                 Pageable pageable,
                                                                                 @RequestParam(value = "min")
                                                                                 Double minTemperature,
                                                                                 @RequestParam(value = "max")
                                                                                 Double maxTemperature) {
        return ResponseEntity.ok(
                postUseCase.getMyPostsByTemperature(memberId, minTemperature, maxTemperature, pageable));
    }

    @PostMapping("/posts/{postId}/reports")
    public ResponseEntity<Void> reportPost(@Login Long memberId, @PathVariable Long postId) {
        postUseCase.reportPost(memberId, postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/liking-posts")
    public ResponseEntity<LikedPostResponses> getLikedPost(@Login Long memberId, Pageable pageable) {
        return ResponseEntity.ok(postUseCase.getLikedPost(memberId, pageable));
    }
}
