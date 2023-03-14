package dandi.dandi.post.web;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.post.web.in.PostRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "게시글")
public interface PostControllerDocs {

    @Operation(summary = "게시글 이미지 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 이미지 등록 성공", headers = {
                    @Header(name = HttpHeaders.LOCATION)}),
            @ApiResponse(responseCode = "500", description = "프로필 사진 변경 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> registerPostImage(@Parameter(hidden = true) Long memberId,
                                           @Parameter(hidden = true) MultipartFile profileImage);

    @Operation(summary = "닉네임 변경", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "닉네임 정상 변경", headers = {
                    @Header(name = HttpHeaders.LOCATION)}),
            @ApiResponse(responseCode = "400", description = "입력 값 중에 null 혹은 빈문자열이 존재할 경우",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> registerPost(@Parameter(hidden = true) Long memberId, PostRegisterRequest postRegisterRequest);
}
