package dandi.dandi.post.adapter.in.web;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.post.application.port.in.FeedResponse;
import dandi.dandi.post.application.port.in.LikedPostResponses;
import dandi.dandi.post.application.port.in.MyPostResponses;
import dandi.dandi.post.application.port.in.MyPostsByTemperatureResponses;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "게시글")
public interface PostQueryControllerDocs {

    @Operation(summary = "게시글 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 상세 조회 정상 응답"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PostDetailResponse> getPostDetails(@Parameter(hidden = true) Long memberId, Long postId);

    @Operation(summary = "내가 올린 게시글", parameters = {@Parameter(name = "size"), @Parameter(name = "page"),
            @Parameter(name = "sort"), @Parameter(example = "DESC")})
    @ApiResponse(responseCode = "200", description = "내가 올린 게시글 정상 반환")
    ResponseEntity<MyPostResponses> getMyPostIdsAndPostImageUrls(@Parameter(hidden = true) Long memberId,
                                                                 @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "기온에 따른 게시글 조회(피드)", parameters = {
            @Parameter(name = "size"), @Parameter(name = "page"),
            @Parameter(name = "sort"), @Parameter(example = "DESC")})
    @ApiResponse(responseCode = "200", description = "기온에 따른 게시글 정상 반환")
    ResponseEntity<FeedResponse> getFeedsByTemperature(@Parameter(hidden = true) Long memberId,
                                                       @Parameter(hidden = true) Pageable pageable,
                                                       @RequestParam(value = "min") Double minTemperature,
                                                       @RequestParam(value = "max") Double maxTemperature);

    @Operation(summary = "기온에 따른 내 게시글 조회", parameters = {
            @Parameter(name = "size"), @Parameter(name = "page"),
            @Parameter(name = "sort"), @Parameter(example = "DESC")})
    @ApiResponse(responseCode = "200", description = "기온에 따른 내 게시글 정상 반환")
    ResponseEntity<MyPostsByTemperatureResponses> getMyPostsByTemperature(@Parameter(hidden = true) Long memberId,
                                                                          @Parameter(hidden = true) Pageable pageable,
                                                                          @RequestParam(value = "min")
                                                                          Double minTemperature,
                                                                          @RequestParam(value = "max")
                                                                          Double maxTemperature);

    @Operation(summary = "좋아요 누른 게시글 조회", parameters = {
            @Parameter(name = "size"), @Parameter(name = "page"),
            @Parameter(name = "sort"), @Parameter(example = "DESC")})
    @ApiResponse(responseCode = "200", description = "좋아요 누른 게시글 정상 응답")
    ResponseEntity<LikedPostResponses> getLikedPost(@Parameter(hidden = true) Long memberId,
                                                    @Parameter(hidden = true) Pageable pageable);
}
