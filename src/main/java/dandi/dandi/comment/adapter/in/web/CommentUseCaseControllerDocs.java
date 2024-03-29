package dandi.dandi.comment.adapter.in.web;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.comment.application.port.in.CommentRegisterCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "댓글")
public interface CommentUseCaseControllerDocs {

    @Operation(summary = "댓글 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글에 댓글 작성",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> registerComment(@Parameter(hidden = true) Long memberId, @PathVariable Long postId,
                                         @RequestBody CommentRegisterCommand commentRegisterCommand);


    @Operation(summary = "댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글 삭제 요청",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "다른 사용자의 댓글 삭제 요청",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    }

    )
    ResponseEntity<Void> deleteComment(@Parameter(hidden = true) Long memberId, @PathVariable Long commentId);
}
