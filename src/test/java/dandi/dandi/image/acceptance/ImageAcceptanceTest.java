package dandi.dandi.image;

import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorizationAndImgFile;
import static dandi.dandi.common.RequestURI.CLOTHES_IMAGE_REGISTER_REQUEST_URI;
import static dandi.dandi.utils.image.TestImageUtils.TEST_IMAGE_RESOURCE_DIR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import dandi.dandi.common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ImageAcceptanceTest extends AcceptanceTest {

    @DisplayName("1MB 보다 큰 사진 파일을 등록하려 하면 400을 응답한다.")
    @Test
    void postImage_BadRequest() {
        String token = getToken();
        File file = new File(new File("")
                .getAbsolutePath() + TEST_IMAGE_RESOURCE_DIR + "biggerThanLimit.jpg");
        String multiPartControlName = "clothesImage";

        ExtractableResponse<Response> response = httpPostWithAuthorizationAndImgFile(
                CLOTHES_IMAGE_REGISTER_REQUEST_URI, token, file, multiPartControlName);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
