package dandi.dandi.post.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.post.application.port.in.PostReportCommandServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostReportCommandController implements PostReportCommandControllerDocs {

    private final PostReportCommandServicePort postReportCommandServicePort;

    public PostReportCommandController(PostReportCommandServicePort postReportCommandServicePort) {
        this.postReportCommandServicePort = postReportCommandServicePort;
    }

    @PostMapping("/posts/{postId}/reports")
    public ResponseEntity<Void> reportPost(@Login Long memberId, @PathVariable Long postId) {
        postReportCommandServicePort.reportPost(memberId, postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
