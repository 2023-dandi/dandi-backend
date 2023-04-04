package dandi.dandi.common.exception;

public class ForbiddenException extends RuntimeException {

    private static final String POST_DELETION_FORBIDDEN_EXCEPTION_MESSAGE = "게시글을 삭제할 수 있는 권한이 없습니다.";
    private static final String CLOTHES_DELETION_FORBIDDEN_EXCEPTION_MESSAGE = "옷을 삭제할 수 있는 권한이 없습니다.";

    public ForbiddenException(String message) {
        super(message);
    }

    public static ForbiddenException postDeletion() {
        return new ForbiddenException(POST_DELETION_FORBIDDEN_EXCEPTION_MESSAGE);
    }

    public static ForbiddenException clothesDeletion() {
        return new ForbiddenException(CLOTHES_DELETION_FORBIDDEN_EXCEPTION_MESSAGE);
    }

    public static ForbiddenException clothesLookUp() {
        return new ForbiddenException("다른 사람의 옷은 조회할 수 없습니다.");
    }

    public static ForbiddenException commentDeletion() {
        return new ForbiddenException("댓글을 삭제할 수 있는 권한이 없습니다.");
    }
}
