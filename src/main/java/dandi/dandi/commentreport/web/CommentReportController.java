package dandi.dandi.commentreport.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.commentreport.application.port.in.CommentReportUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentReportController {

    private final CommentReportUseCase commentReportUseCase;

    public CommentReportController(CommentReportUseCase commentReportUseCase) {
        this.commentReportUseCase = commentReportUseCase;
    }

    @PostMapping("/comments/{commentId}/reports")
    public ResponseEntity<Void> reportComment(@Login Long memberId, @PathVariable Long commentId) {
        commentReportUseCase.reportComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
