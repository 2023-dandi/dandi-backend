package dandi.dandi.advice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import dandi.dandi.auth.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionResponseHandler.class);
    private static final String INTERNAL_SERVER_ERROR_LOGGING_FORMAT = "[500]" + System.lineSeparator() + "{}";

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> unauthorized(UnauthorizedException exception) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> badRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler({InternalServerException.class, RuntimeException.class})
    public ResponseEntity<ExceptionResponse> internalServerError(RuntimeException e) {
        logger.warn(INTERNAL_SERVER_ERROR_LOGGING_FORMAT, e.getMessage());
        return ResponseEntity.internalServerError()
                .body(ExceptionResponse.internalServerError());
    }
}
