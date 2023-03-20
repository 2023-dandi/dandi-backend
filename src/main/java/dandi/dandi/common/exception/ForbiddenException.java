package dandi.dandi.common.exception;

public class ForbiddenException extends RuntimeException {

    private static final String POST_DELETION_FORBIDDEN_EXCEPTION_MESSAGE = "게시글을 삭제할 수 있는 권한이 없습니다.";

    public ForbiddenException(String message) {
        super(message);
    }

    public static ForbiddenException postDeletion() {
        return new ForbiddenException(POST_DELETION_FORBIDDEN_EXCEPTION_MESSAGE);
    }
}
