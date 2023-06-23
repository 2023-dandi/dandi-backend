package dandi.dandi.member.adapter.in.web;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.member.application.port.in.ProfileImageUpdateResponse;
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

@Tag(name = "회원체계")
public interface MemberImageControllerDocs {

    @Operation(summary = "프로필 사진 변경", parameters = {
            @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"),
            @Parameter(name = CONTENT_TYPE, in = ParameterIn.HEADER, required = true, example = "multipart/form-data; boundary=------AaB03x")},
            requestBody =
            @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(pattern =
                            "------AaB03x\t\n"
                                    + "Content-Disposition: form-data; name = profileImage; filename = test_img.jpg\t\n"
                                    + "Content-Type: application/octet-stream\t\n"
                                    + "\t\n"
                                    + "파일\t\n"
                                    + "------AaB03x"), encoding = @Encoding(name = "profileImage", contentType = IMAGE_JPEG_VALUE)))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 사진 정상 변경", content = @Content(schema = @Schema(implementation = ProfileImageUpdateResponse.class))),
            @ApiResponse(responseCode = "500", description = "프로필 사진 변경 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<ProfileImageUpdateResponse> updateMemberProfileImage(@Parameter(hidden = true) Long memberId,
                                                                        @Parameter(hidden = true) MultipartFile profileImage);
}
