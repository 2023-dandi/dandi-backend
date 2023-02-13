package dandi.dandi.common;

import dandi.dandi.advice.ExceptionResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class HttpResponseExtractor {

    public static String extractExceptionMessage(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", ExceptionResponse.class)
                .getMessage();
    }
}
