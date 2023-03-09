package dandi.dandi.member.web;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.port.in.ProfileImageUpdateResponse;
import dandi.dandi.member.web.dto.in.LocationUpdateRequest;
import dandi.dandi.member.web.dto.in.NicknameUpdateRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "회원체계")
public interface MemberControllerDocs {

    @Operation(summary = "사용자 정보 반환", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponse(responseCode = "200", description = "사용자 정보 정상 반환")
    ResponseEntity<MemberInfoResponse> getMemberInfo(@Parameter(hidden = true) Long memberId);

    @Operation(summary = "닉네임 중복 확인")
    @ApiResponse(responseCode = "200", description = "닉네임 중복 확인 성공")
    ResponseEntity<NicknameDuplicationCheckResponse> checkNicknameDuplication(@RequestParam String nickname);

    @Operation(summary = "닉네임 변경", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "닉네임 정상 변경"),
            @ApiResponse(responseCode = "400", description = "규칙에 어긋나는 닉네임\t\n"
                    + "이미 존재하는 닉네임",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> updateMemberNickname(@Parameter(hidden = true) Long memberId,
                                              NicknameUpdateRequest nicknameUpdateRequest);

    @Operation(summary = "위치 변경", parameters = @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, required = true, example = "Bearer ${token}"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "위치 정상 변경"),
            @ApiResponse(responseCode = "400", description = "범위에 어긋나는 경도, 위도",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> updateMemberLocation(@Parameter(hidden = true) Long memberId,
                                              LocationUpdateRequest locationUpdateRequest);

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
