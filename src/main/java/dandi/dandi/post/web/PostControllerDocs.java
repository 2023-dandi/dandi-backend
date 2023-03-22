package dandi.dandi.post.web;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.post.application.port.in.MyPostResponses;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostImageRegisterResponse;
import dandi.dandi.post.application.port.in.PostRegisterResponse;
import dandi.dandi.post.web.in.PostRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "게시글")
public interface PostControllerDocs {

    @Operation(summary = "게시글 이미지 등록",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(pattern =
                            "------AaB03x\r\n"
                                    + "Content-Disposition: form-data; name = postImage; filename = test_img.jpg\r\n"
                                    + "Content-Type: application/octet-stream\r\n"
                                    + "\r\n"
                                    + "파일\r\n"
                                    + "------AaB03x"), encoding = @Encoding(name = "postImage", contentType = IMAGE_JPEG_VALUE))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 이미지 등록 성공"),
            @ApiResponse(responseCode = "500", description = "프로필 사진 변경 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PostImageRegisterResponse> registerPostImage(@Parameter(hidden = true) Long memberId,
                                                                @Parameter(hidden = true) MultipartFile profileImage);

    @Operation(summary = "게시글 등록", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "닉네임 정상 변경"),
            @ApiResponse(responseCode = "400", description = "입력 값 중에 null 혹은 빈문자열이 존재할 경우 \t\n"
                    + "착장 느낌 Index가 0 ~ 4 범위가 아닌 경우",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PostRegisterResponse> registerPost(@Parameter(hidden = true) Long memberId,
                                                      PostRegisterRequest postRegisterRequest);

    @Operation(summary = "게시글 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 상세 조회 정상 응답"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PostDetailResponse> getPostDetails(@Parameter(hidden = true) Long memberId, Long postId);

    @Operation(summary = "게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "자신이 작성하지 않은 게시글 삭제 요청",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> deletePost(@Parameter(hidden = true) Long memberId, Long postId);

    @Operation(summary = "내가 올린 게시글")
    @ApiResponse(responseCode = "200", description = "내가 올린 게시글 정상 반환")
    ResponseEntity<MyPostResponses> getMyPostIdsAndPostImageUrls(@Parameter(hidden = true) Long memberId);
}
