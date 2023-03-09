package dandi.dandi.advice;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExceptionResponse {

    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public static ExceptionResponse internalServerError() {
        return new ExceptionResponse("서버에 알 수 없는 문제가 발생했습니다.");
    }

    public String getMessage() {
        return message;
    }
}
