package dandi.dandi.post.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.post.application.port.in.PostImageCommandPort;
import dandi.dandi.post.application.port.in.PostImageRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PostImageController implements PostImageControllerDocs {

    private final PostImageCommandPort postImageCommandPort;

    public PostImageController(PostImageCommandPort postImageCommandPort) {
        this.postImageCommandPort = postImageCommandPort;
    }

    @PostMapping("/posts/images")
    public ResponseEntity<PostImageRegisterResponse> registerPostImage(@Login Long memberId,
                                                                       @RequestPart(value = "postImage")
                                                                       MultipartFile profileImage) {
        PostImageRegisterResponse postImageRegisterResponse =
                postImageCommandPort.uploadPostImage(memberId, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(postImageRegisterResponse);
    }
}
