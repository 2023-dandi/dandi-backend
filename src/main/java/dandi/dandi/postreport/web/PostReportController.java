package dandi.dandi.postreport.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.postreport.application.port.in.PostReportUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostReportController {

    private final PostReportUseCase postReportUseCase;

    public PostReportController(PostReportUseCase postReportUseCase) {
        this.postReportUseCase = postReportUseCase;
    }

    @PostMapping("/posts/{postId}/reports")
    public ResponseEntity<Void> reportPost(@Login Long memberId, @PathVariable Long postId) {
        postReportUseCase.reportPost(memberId, postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
