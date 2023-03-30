package dandi.dandi.common.exception;

public class NotFoundException extends RuntimeException {

    private static final String NOT_FOUND_MESSAGE_FORMAT = "존재하지 않는 %s입니다.";
    private static final String POST_RESOURCE_NAME = "게시글";
    private static final String CLOTHES_RESOURCE_NAME = "옷";

    private NotFoundException(String resourceName) {
        super(String.format(NOT_FOUND_MESSAGE_FORMAT, resourceName));
    }

    public static NotFoundException post() {
        return new NotFoundException(POST_RESOURCE_NAME);
    }

    public static NotFoundException clothes() {
        return new NotFoundException(CLOTHES_RESOURCE_NAME);
    }
}
