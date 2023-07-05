package dandi.dandi.clothes.acceptance;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES_CATEGORY;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_FULL_URL;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_URL;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_SEASONS;
import static dandi.dandi.clothes.domain.Category.BOTTOM;
import static dandi.dandi.clothes.domain.Category.TOP;
import static dandi.dandi.common.HttpMethodFixture.httpDeleteWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpGetWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorizationAndImgFile;
import static dandi.dandi.common.RequestURI.CLOTHES_CATEGORIES_URI;
import static dandi.dandi.common.RequestURI.CLOTHES_IMAGE_REGISTER_REQUEST_URI;
import static dandi.dandi.common.RequestURI.CLOTHES_REQUEST_URI;
import static dandi.dandi.utils.TestImageUtils.generateTestImgFile;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import dandi.dandi.clothes.application.port.in.CategorySeasonsResponse;
import dandi.dandi.clothes.application.port.in.CategorySeasonsResponses;
import dandi.dandi.clothes.application.port.in.ClothesDetailResponse;
import dandi.dandi.clothes.application.port.in.ClothesImageRegisterResponse;
import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.in.ClothesResponse;
import dandi.dandi.clothes.application.port.in.ClothesResponses;
import dandi.dandi.common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

class ClothesAcceptanceTest extends AcceptanceTest {

    @DisplayName("옷 사진 등록 요청에 성공하면 201과 옷 사진 url을 응답한다.")
    @Test
    void registerClothesImage_Created() {
        String token = getToken();
        File file = generateTestImgFile();
        String multiPartControlName = "clothesImage";

        ExtractableResponse<Response> response = httpPostWithAuthorizationAndImgFile(
                CLOTHES_IMAGE_REGISTER_REQUEST_URI, token, file, multiPartControlName);

        ClothesImageRegisterResponse clothesImageRegisterResponse = response.jsonPath()
                .getObject(".", ClothesImageRegisterResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(clothesImageRegisterResponse.getClothesImageUrl())
                        .startsWith("clothes/").endsWith(".jpg")
        );
    }

    @DisplayName("옷 사진 등록 요청에 성공하면 500을 응답한다.")
    @Test
    void registerClothesImage_InternalServerError() {
        String token = getToken();
        File file = generateTestImgFile();
        String multiPartControlName = "clothesImage";
        mockAmazonS3Exception();

        ExtractableResponse<Response> response = httpPostWithAuthorizationAndImgFile(
                CLOTHES_IMAGE_REGISTER_REQUEST_URI, token, file, multiPartControlName);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("옷장에 옷 등록 요청에 성공하면 201을 응답한다.")
    @Test
    void registerClothes_Created() {
        String token = getToken();
        ClothesRegisterCommand clothesRegisterCommand =
                new ClothesRegisterCommand(CLOTHES_CATEGORY, CLOTHES_SEASONS, CLOTHES_IMAGE_FULL_URL);

        ExtractableResponse<Response> response =
                httpPostWithAuthorization(CLOTHES_REQUEST_URI, clothesRegisterCommand, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("유효하지 않은 카테고리, 계절 값이 포함된 옷 등록 요청에 400을 응답한다.")
    @ParameterizedTest
    @CsvSource({"TOPPP, SUMMERRRR", "TOP, SUMMBERRR", "TOPPP, SUMMER"})
    void registerClothes_BadRequest(String category, String season) {
        String token = getToken();
        ClothesRegisterCommand invalidClothesRegisterCommand =
                new ClothesRegisterCommand(category, List.of(season), CLOTHES_IMAGE_FULL_URL);

        ExtractableResponse<Response> response =
                httpPostWithAuthorization(CLOTHES_REQUEST_URI, invalidClothesRegisterCommand, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("옷 상세 조회 요청에 성공하면 200과 옷 상세정보를 응답한다.")
    @Test
    void getSingleClothesDetails_OK() {
        String token = getToken();
        registerClothes(token);
        Long clothesId = 1L;

        ExtractableResponse<Response> response = httpGetWithAuthorization(CLOTHES_REQUEST_URI + "/" + clothesId, token);

        ClothesDetailResponse clothesDetailResponse = response.jsonPath()
                .getObject(".", ClothesDetailResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(clothesDetailResponse.getId()).isEqualTo(clothesId),
                () -> assertThat(clothesDetailResponse.getClothesImageUrl()).isEqualTo(CLOTHES_IMAGE_FULL_URL)
        );
    }

    @DisplayName("다른 사용자의 옷 상세 조회 요청에 대해 403을 응답한다.")
    @Test
    void getSingleClothesDetails_Forbidden() {
        String token = getToken();
        String anotherToken = getAnotherMemberToken();
        registerClothes(token);
        Long clothesId = 1L;

        ExtractableResponse<Response> response =
                httpGetWithAuthorization(CLOTHES_REQUEST_URI + "/" + clothesId, anotherToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("옷 카테고리, 계절 조회 요청에 성공하면 200고 옷들을 반환한다.")
    @Test
    void getCategories_OK() {
        String token = getToken();
        httpPostWithAuthorization(CLOTHES_REQUEST_URI, new ClothesRegisterCommand(
                CLOTHES_CATEGORY, List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_FULL_URL), token);
        httpPostWithAuthorization(CLOTHES_REQUEST_URI, new ClothesRegisterCommand(
                "BOTTOM", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_FULL_URL), token);
        httpPostWithAuthorization(CLOTHES_REQUEST_URI, new ClothesRegisterCommand(
                CLOTHES_CATEGORY, List.of("SPRING", "FALL"), CLOTHES_IMAGE_FULL_URL), token);
        httpPostWithAuthorization(CLOTHES_REQUEST_URI, new ClothesRegisterCommand(
                CLOTHES_CATEGORY, List.of("FALL", "WINTER"), CLOTHES_IMAGE_FULL_URL), token);

        ExtractableResponse<Response> response = httpGetWithAuthorization(CLOTHES_CATEGORIES_URI, token);

        List<CategorySeasonsResponse> categories = response.jsonPath()
                .getObject(".", CategorySeasonsResponses.class)
                .getCategories();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(categories).hasSize(3),
                () -> assertThat(categories.get(0).getCategory()).isEqualTo("ALL"),
                () -> assertThat(categories.get(0).getSeasons()).hasSize(4),
                () -> assertThat(categories.get(1).getCategory()).isEqualTo(TOP.name()),
                () -> assertThat(categories.get(1).getSeasons()).hasSize(4),
                () -> assertThat(categories.get(2).getCategory()).isEqualTo(BOTTOM.name()),
                () -> assertThat(categories.get(2).getSeasons()).hasSize(2)
        );
    }

    @DisplayName("카테고리, 계절에 따른 옷 조회 요청에 성공하면 200과 옷들을 반환한다.")
    @ParameterizedTest
    @CsvSource({"TOP, 2", "BOTTOM, 1", "ALL, 3"})
    void getClothes(String category, int expectedSize) {
        String token = getToken();
        httpPostWithAuthorization(CLOTHES_REQUEST_URI, new ClothesRegisterCommand(
                CLOTHES_CATEGORY, List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL), token);
        httpPostWithAuthorization(CLOTHES_REQUEST_URI, new ClothesRegisterCommand(
                CLOTHES_CATEGORY, List.of("SPRING", "FALL"), CLOTHES_IMAGE_URL), token);
        httpPostWithAuthorization(CLOTHES_REQUEST_URI, new ClothesRegisterCommand(
                CLOTHES_CATEGORY, List.of("FALL", "WINTER"), CLOTHES_IMAGE_URL), token);
        httpPostWithAuthorization(CLOTHES_REQUEST_URI, new ClothesRegisterCommand(
                "BOTTOM", List.of("SPRING", "SUMMER"), CLOTHES_IMAGE_URL), token);
        String queryString = "?category=" + category + "&season=SUMMER&season=SPRING";

        ExtractableResponse<Response> response = httpGetWithAuthorization(CLOTHES_REQUEST_URI + queryString, token);

        ClothesResponses clothesResponses = response.jsonPath()
                .getObject(".", ClothesResponses.class);
        List<ClothesResponse> clothesResponse = clothesResponses.getClothes();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(clothesResponse).hasSize(expectedSize),
                () -> assertThat(clothesResponses.isLastPage()).isTrue(),
                () -> assertThat(clothesResponse.get(0).getClothesImageUrl()).isEqualTo(CLOTHES_IMAGE_FULL_URL)
        );
    }

    @DisplayName("옷 삭제 요청에 성공하면 204를 응답한다.")
    @Test
    void deleteClothes_NoContent() {
        String token = getToken();
        registerClothes(token);

        ExtractableResponse<Response> response = httpDeleteWithAuthorization(CLOTHES_REQUEST_URI + "/1", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 옷 삭제 요청에 404를 응답한다.")
    @Test
    void deleteClothes_NotFound() {
        String token = getToken();

        ExtractableResponse<Response> response = httpDeleteWithAuthorization(CLOTHES_REQUEST_URI + "/1", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("다른 사람의 옷 삭제 요청에 403을 응답한다.")
    @Test
    void deleteClothes_Forbidden() {
        String token = getToken();
        registerClothes(token);
        String anotherMemberToken = getAnotherMemberToken();

        ExtractableResponse<Response> response =
                httpDeleteWithAuthorization(CLOTHES_REQUEST_URI + "/1", anotherMemberToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("오늘의 옷 조회 요청에 대해 200과 오늘의 옷을 응답한다.")
    @Test
    void getTodayClothes() {
        String token = getToken();
        registerClothes(token, List.of("SUMMER", "FALL"));
        registerClothes(token, List.of("FALL", "WINTER"));
        registerClothes(token, List.of("SPRING", "SUMMER"));
        when(clock.instant())
                .thenReturn(Instant.parse("2023-11-22T10:00:00Z"));

        ExtractableResponse<Response> response = httpGetWithAuthorization("/clothes/today", token);

        List<ClothesResponse> clothes = response.jsonPath()
                .getObject(".", ClothesResponses.class)
                .getClothes();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(clothes).hasSize(2),
                () -> assertThat(clothes.get(0).getClothesImageUrl()).isEqualTo(CLOTHES_IMAGE_FULL_URL)
        );
    }

    private void registerClothes(String token, List<String> seasons) {
        ClothesRegisterCommand clothesRegisterCommand =
                new ClothesRegisterCommand(CLOTHES_CATEGORY, seasons, CLOTHES_IMAGE_URL);
        httpPostWithAuthorization(CLOTHES_REQUEST_URI, clothesRegisterCommand, token);
    }

    private void registerClothes(String token) {
        ClothesRegisterCommand clothesRegisterCommand =
                new ClothesRegisterCommand(CLOTHES_CATEGORY, CLOTHES_SEASONS, CLOTHES_IMAGE_URL);
        httpPostWithAuthorization(CLOTHES_REQUEST_URI, clothesRegisterCommand, token);
    }
}
