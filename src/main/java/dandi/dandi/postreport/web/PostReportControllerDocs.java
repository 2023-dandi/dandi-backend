package dandi.dandi.postreport.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "게시글 신고")
public interface PostReportControllerDocs {

    @Operation(summary = "게시글 신고")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 신고 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글"),
            @ApiResponse(responseCode = "400", description = "이미 신고한 게시글"),
    })
    ResponseEntity<Void> reportPost(@Parameter(hidden = true) Long memberId, @PathVariable Long postId);
}
