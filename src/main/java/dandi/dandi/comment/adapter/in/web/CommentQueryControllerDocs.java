package dandi.dandi.comment.adapter.in.web;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.comment.application.port.in.CommentResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "댓글")
public interface CommentQueryControllerDocs {

    @Operation(summary = "댓글 조회", parameters = {
            @Parameter(name = "size"), @Parameter(name = "page"),
            @Parameter(name = "sort"), @Parameter(example = "DESC")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글에 대한 댓글 조회 요청",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<CommentResponses> getComments(@Parameter(hidden = true) Long memberId, @PathVariable Long postId,
                                                 @Parameter(hidden = true) Pageable pageable);
}
