package dandi.dandi.comment.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.comment.application.port.in.CommentReportUseCaseServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentReportUseCaseController implements CommentReportUseCaseControllerDocs {

    private final CommentReportUseCaseServicePort commentReportUseCaseServicePort;

    public CommentReportUseCaseController(CommentReportUseCaseServicePort commentReportUseCaseServicePort) {
        this.commentReportUseCaseServicePort = commentReportUseCaseServicePort;
    }

    @PostMapping("/comments/{commentId}/reports")
    public ResponseEntity<Void> reportComment(@Login Long memberId, @PathVariable Long commentId) {
        commentReportUseCaseServicePort.reportComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
