package dandi.dandi.postlike.adapter.in.web;

import dandi.dandi.advice.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "게시글 좋아요")
public interface PostLikeCommandControllerDocs {

    @Operation(summary = "좋아요 등록/취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "좋아요 정상 등록/취소"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글에 좋아요 등록/취소",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> reverseLikes(@Parameter(hidden = true) Long memberId, @PathVariable Long postId);
}
