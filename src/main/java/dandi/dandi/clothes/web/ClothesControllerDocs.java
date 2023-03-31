package dandi.dandi.clothes.web;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.clothes.application.port.in.ClothesImageRegisterResponse;
import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "옷장")
public interface ClothesControllerDocs {

    @Operation(summary = "옷 이미지 등록",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(pattern =
                            "------AaB03x\r\n"
                                    + "Content-Disposition: form-data; name = clothesImage; filename = test_img.jpg\r\n"
                                    + "Content-Type: application/octet-stream\r\n"
                                    + "\r\n"
                                    + "파일\r\n"
                                    + "------AaB03x"), encoding = @Encoding(name = "clothesImage", contentType = IMAGE_JPEG_VALUE))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "옷 이미지 등록 성공"),
            @ApiResponse(responseCode = "500", description = "옷 이미지 등록 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<ClothesImageRegisterResponse> registerClothesImage(@Parameter(hidden = true) Long memberId,
                                                                      @Parameter(hidden = true)
                                                                      MultipartFile clothesImage);

    @Operation(summary = "옷 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "옷 이미지 등록 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 계절 혹은 옷 카테고리",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> registerClothes(@Parameter(hidden = true) Long memberId,
                                         ClothesRegisterCommand clothesRegisterCommand);

    @Operation(summary = "옷 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "옷 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 옷 삭제",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "다른 사람의 옷 삭제",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> deleteClothes(@Parameter(hidden = true) Long memberId, @PathVariable Long clothesId);
}
