package dandi.dandi.advice;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExceptionResponse {

    @Schema(example = "Exception Message")
    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
