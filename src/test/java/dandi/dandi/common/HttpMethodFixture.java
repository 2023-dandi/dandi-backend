package dandi.dandi.common;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.http.MediaType;

public class HttpMethodFixture {

    private static final String AUTHORIZATION_TYPE = "Bearer ";

    public static ExtractableResponse<Response> httpPost(Object requestBody, String path) {
        return RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPostWithAuthorizationAndCookie(String path, String token,
                                                                                   Map<String, Object> cookies) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, AUTHORIZATION_TYPE + token)
                .cookies(cookies)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpGetWithAuthorization(String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, AUTHORIZATION_TYPE + token)
                .when().get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPatchWithAuthorization(String path,
                                                                           Object requestBody,
                                                                           String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, AUTHORIZATION_TYPE + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().patch(path)
                .then().log().all()
                .extract();
    }
}
