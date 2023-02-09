package dandi.dandi.advice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import dandi.dandi.auth.exception.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> unauthorized(UnauthorizedException exception) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new ExceptionResponse(exception.getMessage()));
    }
}
