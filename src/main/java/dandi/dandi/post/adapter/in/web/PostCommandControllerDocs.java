package dandi.dandi.post.adapter.in.web;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.post.adapter.in.web.dto.PostRegisterRequest;
import dandi.dandi.post.application.port.in.PostRegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "게시글")
public interface PostCommandControllerDocs {

    @Operation(summary = "게시글 등록", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "닉네임 정상 변경"),
            @ApiResponse(responseCode = "400", description = "입력 값 중에 null 혹은 빈문자열이 존재할 경우 \t\n"
                    + "착장 느낌 Index가 0 ~ 4 범위가 아닌 경우",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PostRegisterResponse> registerPost(@Parameter(hidden = true) Long memberId,
                                                      PostRegisterRequest postRegisterRequest);

    @Operation(summary = "게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "자신이 작성하지 않은 게시글 삭제 요청",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> deletePost(@Parameter(hidden = true) Long memberId, Long postId);
}
