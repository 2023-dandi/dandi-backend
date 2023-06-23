package dandi.dandi.post.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.post.application.port.in.FeedResponse;
import dandi.dandi.post.application.port.in.LikedPostResponses;
import dandi.dandi.post.application.port.in.MyPostResponses;
import dandi.dandi.post.application.port.in.MyPostsByTemperatureResponses;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostQueryServicePort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostQueryController implements PostQueryControllerDocs {

    private final PostQueryServicePort postQueryServicePort;

    public PostQueryController(PostQueryServicePort postQueryServicePort) {
        this.postQueryServicePort = postQueryServicePort;
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetails(@Login Long memberId, @PathVariable Long postId) {
        return ResponseEntity.ok(postQueryServicePort.getPostDetails(memberId, postId));
    }

    @GetMapping(path = "/posts/my")
    public ResponseEntity<MyPostResponses> getMyPostIdsAndPostImageUrls(@Login Long memberId,
                                                                        @PageableDefault(size = 500, sort = "createdAt",
                                                                                direction = Direction.DESC)
                                                                        Pageable pageable) {
        return ResponseEntity.ok(postQueryServicePort.getMyPostIdsAndPostImageUrls(memberId, pageable));
    }

    @GetMapping("/posts/feed/temperature")
    public ResponseEntity<FeedResponse> getFeedsByTemperature(@Login Long memberId, Pageable pageable,
                                                              @RequestParam(value = "min") Double minTemperature,
                                                              @RequestParam(value = "max") Double maxTemperature) {
        return ResponseEntity.ok(
                postQueryServicePort.getPostsByTemperature(memberId, minTemperature, maxTemperature, pageable));
    }

    @GetMapping("/posts/my/temperature")
    public ResponseEntity<MyPostsByTemperatureResponses> getMyPostsByTemperature(@Login Long memberId,
                                                                                 Pageable pageable,
                                                                                 @RequestParam(value = "min")
                                                                                 Double minTemperature,
                                                                                 @RequestParam(value = "max")
                                                                                 Double maxTemperature) {
        return ResponseEntity.ok(
                postQueryServicePort.getMyPostsByTemperature(memberId, minTemperature, maxTemperature, pageable));
    }

    @GetMapping("/liking-posts")
    public ResponseEntity<LikedPostResponses> getLikedPost(@Login Long memberId, Pageable pageable) {
        return ResponseEntity.ok(postQueryServicePort.getLikedPost(memberId, pageable));
    }
}
