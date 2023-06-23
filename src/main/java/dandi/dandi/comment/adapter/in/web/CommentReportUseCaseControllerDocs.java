package dandi.dandi.comment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "댓글")
public interface CommentReportUseCaseControllerDocs {

    @Operation(summary = "댓글 신고")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 신고 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글"),
            @ApiResponse(responseCode = "400", description = "이미 신고한 댓글"),
    })
    ResponseEntity<Void> reportComment(@Parameter(hidden = true) Long memberId, @PathVariable Long commentId);
}
