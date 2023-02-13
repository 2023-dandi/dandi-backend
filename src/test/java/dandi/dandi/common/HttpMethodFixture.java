package dandi.dandi.common;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class HttpMethodFixture {

    public static ExtractableResponse<Response> httpPost(Object requestBody, String path) {
        return RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpGetWithAuthorization(String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .when().get(path)
                .then().log().all()
                .extract();
    }
}
