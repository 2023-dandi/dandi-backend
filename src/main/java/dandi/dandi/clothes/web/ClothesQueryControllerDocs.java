package dandi.dandi.clothes.web;

import dandi.dandi.advice.ExceptionResponse;
import dandi.dandi.clothes.application.port.in.CategorySeasonsResponses;
import dandi.dandi.clothes.application.port.in.ClothesDetailResponse;
import dandi.dandi.clothes.application.port.in.ClothesResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "옷장")
public interface ClothesQueryControllerDocs {

    @Operation(summary = "옷들의 카테고리-계절 조회")
    @ApiResponse(responseCode = "200", description = "옷들의 카테고리-계절 조회 성공")
    ResponseEntity<CategorySeasonsResponses> getCategoriesAndSeasons(@Parameter(hidden = true) Long memberId);

    @Operation(summary = "옷 상세조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "옷 상세조회 성공"),
            @ApiResponse(responseCode = "403", description = "다른 사용자의 상세 조회 요청",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<ClothesDetailResponse> getSingleClothesDetails(@Parameter(hidden = true) Long memberId,
                                                                  @PathVariable Long clothesId);

    @Operation(summary = "옷 조회", parameters = {@Parameter(name = "size"), @Parameter(name = "page"),
            @Parameter(name = "sort"), @Parameter(example = "DESC")})
    @ApiResponse(responseCode = "200", description = "옷 조회 성공")
    ResponseEntity<ClothesResponses> getClothes(@Parameter(hidden = true) Long memberId,
                                                @RequestParam("category") String category,
                                                @RequestParam("season") Set<String> seasons,
                                                @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "계절에 따른 옷 조회", parameters = {@Parameter(name = "size"), @Parameter(name = "page"),
            @Parameter(name = "sort"), @Parameter(example = "DESC")})
    @ApiResponse(responseCode = "200", description = "계절에 따른 옷 조회 성공")
    ResponseEntity<ClothesResponses> getTodayClothes(@Parameter(hidden = true) Long memberId,
                                                     @Parameter(hidden = true) Pageable pageable);
}
