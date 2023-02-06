package dandi.dandi.advice;

public class ExternalServerException extends RuntimeException {

    public ExternalServerException(String message) {
        super(message);
    }
}
