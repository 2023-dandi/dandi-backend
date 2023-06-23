package dandi.dandi.post.adapter.in.web;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.post.application.port.in.PostImageRegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
public interface PostImageControllerDocs {

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
}
