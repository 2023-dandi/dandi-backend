package dandi.dandi.advice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ExceptionResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionResponseHandler.class);
    private static final String INTERNAL_SERVER_ERROR_LOGGING_FORMAT = "[500]" + System.lineSeparator() + "{}";

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> unauthorized(UnauthorizedException exception) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ExceptionResponse> badRequest(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFound(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> forbidden(ForbiddenException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionResponse> badRequestFileSizeLimit() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse("등록 가능한 사진 파일 크기는 최대 1MB입니다."));
    }

    @ExceptionHandler({InternalServerException.class, RuntimeException.class})
    public ResponseEntity<ExceptionResponse> internalServerError(RuntimeException e) {
        logger.warn(INTERNAL_SERVER_ERROR_LOGGING_FORMAT, e.getMessage());
        return ResponseEntity.internalServerError()
                .body(ExceptionResponse.internalServerError());
    }
}
